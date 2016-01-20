package engine.render;

import engine.object3d.Mesh;
import engine.object3d.Object3d;
import engine.object3d.camera.Camera;
import engine.object3d.light.Light;
import engine.shader.ShaderProgram;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private FloatBuffer projectionBuffer;
    private FloatBuffer viewBuffer;
    private FloatBuffer modelBuffer;

    private FloatBuffer lightPosition;
    private FloatBuffer lightColor;
    private float lightAttenuation;
    private float lightAmbient;

    private FloatBuffer cameraPosition;

    private ShaderProgram currentProgram;

    public Renderer() {
        projectionBuffer = BufferUtils.createFloatBuffer(16);
        viewBuffer = BufferUtils.createFloatBuffer(16);
        modelBuffer = BufferUtils.createFloatBuffer(16);

        lightPosition = BufferUtils.createFloatBuffer(3);
        lightColor = BufferUtils.createFloatBuffer(3);

        cameraPosition = BufferUtils.createFloatBuffer(3);
        currentProgram = null;
    }

    public void render(Scene scene, Camera camera) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        camera.getProjection().get(projectionBuffer);
        camera.getView().get(viewBuffer);
        camera.getPosition().get(cameraPosition);

        if (!scene.getLights().isEmpty()) {
            Light light = scene.getLights().get(0);
            light.getPosition().get(lightPosition);
            light.getColor().get(lightColor);
            lightAttenuation = light.getAttenuation();
            lightAmbient = light.getAmbient();

            for (Object3d obj: scene.getLights()) {
                drawObject(obj);
            }

        }

        for (Object3d obj: scene.getObjects()) {
            drawObject(obj);
        }

        clearShaderProgram();
    }

    private void drawObject(Object3d obj) {
        if (obj instanceof Mesh) {
            Mesh mesh = (Mesh) obj;

            updateCurrentShaderProgram(mesh.getMaterial().getProgram());

            //get and send buffer data for current model
            mesh.getModel().get(modelBuffer);
            glUniformMatrix4fv(currentProgram.getUniformLocation("model"), false, modelBuffer);

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
            drawObject(child);
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

    private void updateCurrentShaderProgram(ShaderProgram program) {
        if (currentProgram == null || currentProgram != program) {
            currentProgram = program;
            glUseProgram(currentProgram.getProgramId());

            //re-send buffer data to opengl shader uniforms (the ones for this ShaderProgram)
            glUniformMatrix4fv(currentProgram.getUniformLocation("projection"), false, projectionBuffer);
            glUniformMatrix4fv(currentProgram.getUniformLocation("view"), false, viewBuffer);

            Integer lightPositionLocation = currentProgram.getUniformLocation("lightPosition");
            if (lightPositionLocation != null) {
                glUniform3fv(lightPositionLocation, lightPosition);
            }
            Integer lightColorLocation = currentProgram.getUniformLocation("lightColor");
            if (lightColorLocation != null) {
                glUniform3fv(lightColorLocation, lightColor);
            }
            Integer lightAttenuationLocation = currentProgram.getUniformLocation("lightAttenuation");
            if (lightAttenuationLocation != null) {
                glUniform1f(lightAttenuationLocation, lightAttenuation);
            }
            Integer lightAmbientLocation = currentProgram.getUniformLocation("lightAmbient");
            if (lightAmbientLocation != null) {
                glUniform1f(lightAmbientLocation, lightAmbient);
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
