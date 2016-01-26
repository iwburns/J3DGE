package engine.render;

import engine.material.Material;
import engine.material.PhongMaterial;
import engine.object3d.Mesh;
import engine.object3d.Object3d;
import engine.object3d.Scene;
import engine.object3d.camera.Camera;
import engine.object3d.light.Light;
import engine.shader.ShaderProgram;
import engine.util.Color;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private FloatBuffer projectionBuffer;
    private FloatBuffer viewBuffer;
    private FloatBuffer modelBuffer;

    private FloatBuffer cameraPosition;

    private ShaderProgram currentProgram;

    public Renderer() {
        projectionBuffer = BufferUtils.createFloatBuffer(16);
        viewBuffer = BufferUtils.createFloatBuffer(16);
        modelBuffer = BufferUtils.createFloatBuffer(16);

        cameraPosition = BufferUtils.createFloatBuffer(3);
        currentProgram = null;
    }

    public void render(Scene scene, Camera camera) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        camera.getProjection().get(projectionBuffer);
        camera.getView().get(viewBuffer);
        camera.getPosition().get(cameraPosition);

        if (!scene.getLights().isEmpty()) {

            for (Object3d obj: scene.getLights()) {
                drawObject(obj, scene);
            }

        }

        for (Object3d obj: scene.getObjects()) {
            drawObject(obj, scene);
        }

        clearShaderProgram();
    }

    private void drawObject(Object3d obj, Scene scene) {
        if (obj instanceof Mesh) {
            Mesh mesh = (Mesh) obj;

            //TODO: clean up this material junk
            PhongMaterial phongMaterial = null;
            Material material =  mesh.getMaterial();

            if (material instanceof PhongMaterial) {
                phongMaterial = (PhongMaterial) mesh.getMaterial();
            }

            updateCurrentShaderProgram(mesh.getMaterial().getProgram(), scene);

            //get and send buffer data for current model
            mesh.getModel().get(modelBuffer);
            glUniformMatrix4fv(currentProgram.getUniformLocation("model"), false, modelBuffer);

            if (phongMaterial != null) {
                Integer materialSpecularCoefficientLocation = currentProgram.getUniformLocation("materialSpecularCoefficient");
                if (materialSpecularCoefficientLocation != null) {
                    glUniform1f(materialSpecularCoefficientLocation, phongMaterial.getSpecularCoefficient());
                }
                Integer materialSpecularColorLocation = currentProgram.getUniformLocation("materialSpecularColor");
                if (materialSpecularColorLocation != null) {
                    Color color = phongMaterial.getSpecularColor();
                    glUniform3f(materialSpecularColorLocation, color.getR(), color.getG(), color.getB());
                }
            }

            bindVao(mesh);
            enableVertexAttributes();

            if (mesh.getVao().isIndexed()) {
                //todo: move these into the mesh if possible to clean this up a bit.
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.getVao().getIndicesVbo().getId());
                glDrawElements(mesh.getGeometry().getDrawMode(), mesh.getVao().getIndicesVbo().getSize(), GL_UNSIGNED_SHORT, 0);
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            } else {
                glDrawArrays(mesh.getGeometry().getDrawMode(), 0, mesh.getVao().getVerticesVbo().getSize());
            }

            disableVertexAttributes();
            unbindVao();
        }

        for (Object3d child: obj.getChildren()) {
            drawObject(child, scene);
        }
    }

    private void bindVao(Mesh mesh) {
        glBindVertexArray(mesh.getVao().getId());
    }

    private void unbindVao() {
        glBindVertexArray(0);
    }

    private void enableVertexAttributes() {
        currentProgram.getAttributeLocations().forEach(location -> glEnableVertexAttribArray(location));
    }

    private void disableVertexAttributes() {
        currentProgram.getAttributeLocations().forEach(location -> glDisableVertexAttribArray(location));
    }

    private void updateCurrentShaderProgram(ShaderProgram program, Scene scene) {
        if (currentProgram == null || currentProgram != program) {
            currentProgram = program;
            glUseProgram(currentProgram.getProgramId());

            //re-send buffer data to opengl shader uniforms (the ones for this ShaderProgram)
            glUniformMatrix4fv(currentProgram.getUniformLocation("projection"), false, projectionBuffer);
            glUniformMatrix4fv(currentProgram.getUniformLocation("view"), false, viewBuffer);

            ArrayList<Light> lights = scene.getLights();
            Light currentLight;
            Vector3f lightPosition;
            Color lightColor;
            float lightAttenuation;
            float lightAmbient;

            //todo: don't hardcode this
            //also later, don't copy position, color, attenuation and ambient from the same value into all locations
            for (int i = 0; i < 10; i++) {
                if (i > lights.size() - 1) {
                    Integer lightIsActiveLocation = currentProgram.getUniformLocation("lights[" + i + "].isActive");
                    if (lightIsActiveLocation != null) {
                        glUniform1i(lightIsActiveLocation, 0);
                    }
                } else {
                    currentLight = scene.getLights().get(i);
                    Integer lightIsActiveLocation = currentProgram.getUniformLocation("lights[" + i + "].isActive");
                    if (lightIsActiveLocation != null) {
                        glUniform1i(lightIsActiveLocation, 1);
                    }
                    Integer lightPosLocation = currentProgram.getUniformLocation("lights[" + i + "].position");
                    if (lightPosLocation != null) {
                        lightPosition = currentLight.getWorldPosition();
                        glUniform3f(lightPosLocation, lightPosition.x, lightPosition.y, lightPosition.z);
                    }
                    Integer lightColorLocation = currentProgram.getUniformLocation("lights[" + i + "].color");
                    if (lightColorLocation != null) {
                        lightColor = currentLight.getColor();
                        glUniform3f(lightColorLocation, lightColor.getR(), lightColor.getG(), lightColor.getB());
                    }
                    Integer lightAttenuationLocation = currentProgram.getUniformLocation("lights[" + i + "].attenuation");
                    if (lightAttenuationLocation != null) {
                        lightAttenuation = currentLight.getAttenuation();
                        glUniform1f(lightAttenuationLocation, lightAttenuation);
                    }
                    Integer lightAmbientLocation = currentProgram.getUniformLocation("lights[" + i + "].ambient");
                    if (lightAmbientLocation != null) {
                        lightAmbient = currentLight.getAmbient();
                        glUniform1f(lightAmbientLocation, lightAmbient);
                    }
                }
            }

            Integer cameraPositionLocation = currentProgram.getUniformLocation("cameraPosition");
            if (cameraPositionLocation != null) {
                glUniform3fv(cameraPositionLocation, cameraPosition);
            }
        }
    }

    private void clearShaderProgram() {
        glUseProgram(0);
        currentProgram = null;
    }

    public void destroy() {
        projectionBuffer = null;
        viewBuffer = null;
        modelBuffer = null;
        currentProgram = null;
    }

}
