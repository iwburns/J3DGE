package engine.render;

import engine.object3d.Mesh;
import engine.object3d.Object3d;
import engine.object3d.camera.Camera;
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

    private ShaderProgram currentProgram;

    public Renderer() {
        projectionBuffer = BufferUtils.createFloatBuffer(16);
        viewBuffer = BufferUtils.createFloatBuffer(16);
        modelBuffer = BufferUtils.createFloatBuffer(16);
        currentProgram = null;
    }

    public void render(Scene scene, Camera camera) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        camera.getProjection().get(projectionBuffer);
        camera.getView().get(viewBuffer);

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
                glDrawArrays(mesh.getGeometry().getDrawMode(), 0, mesh.getVao().getVertexBufferObjects().get(0).getSize());
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
