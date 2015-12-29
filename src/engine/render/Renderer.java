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
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Renderer {

    //TODO: add some sort of caching so that we can change ShaderPrograms and send buffer data less often

    private FloatBuffer projectionBuffer;
    private FloatBuffer viewBuffer;
    private FloatBuffer modelBuffer;

    public Renderer() {
        projectionBuffer = BufferUtils.createFloatBuffer(16);
        viewBuffer = BufferUtils.createFloatBuffer(16);
        modelBuffer = BufferUtils.createFloatBuffer(16);
    }

    public void render(Scene scene, Camera camera) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        camera.getProjection().get(projectionBuffer);
        camera.getView().get(viewBuffer);

        for (Object3d obj: scene.getObjects()) {
            drawObject(obj);
        }

        glUseProgram(0);
    }

    private void drawObject(Object3d obj) {
        if (obj instanceof Mesh) {
            Mesh mesh = (Mesh) obj;

            mesh.getModel().get(modelBuffer);

            ShaderProgram program = mesh.getMaterial().getProgram();
            glUseProgram(program.getProgramId());

            //send buffer data to opengl shader uniforms (the ones for this ShaderProgram)
            glUniformMatrix4fv(program.getUniformLocation("projection"), false, projectionBuffer);
            glUniformMatrix4fv(program.getUniformLocation("view"), false, viewBuffer);
            glUniformMatrix4fv(program.getUniformLocation("model"), false, modelBuffer);

            mesh.bindVao();
            mesh.enableVertexAttributes();

            if (mesh.isIndexed()) {
                //todo: move these into the mesh if possible to clean this up a bit.
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.getIndicesVboID());
                glDrawElements(mesh.getGeometry().getDrawMode(), mesh.getIndicesCount(), GL_UNSIGNED_SHORT, 0);
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            } else {
                glDrawArrays(mesh.getGeometry().getDrawMode(), 0, mesh.getVerticesCount());
            }

            mesh.disableVertexAttributes();
            mesh.unbindVao();
        }
    }

    public void destroy() {
        projectionBuffer = null;
        viewBuffer = null;
        modelBuffer = null;
    }

}
