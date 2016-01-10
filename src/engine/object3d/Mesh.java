package engine.object3d;

import engine.geometry.Geometry;
import engine.material.Material;
import engine.opengl.vao.VertexArrayObject;
import engine.opengl.vbo.FloatVertexBufferObject;
import engine.opengl.vbo.ShortVertexBufferObject;
import engine.opengl.vbo.VertexAttributePointer;
import engine.opengl.vbo.VertexBufferObject;
import engine.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

public class Mesh extends Object3d {

    //TODO: check destroy.

    Geometry geometry;
    Material material;

    private VertexArrayObject vao;

    private FloatVertexBufferObject verticesVbo;
    private FloatVertexBufferObject colorsVbo;
    private ShortVertexBufferObject indicesVbo;

    public Mesh(Geometry g, Material m) {
        geometry = g;
        material = m;
        setupBuffers();
    }

    public void disableVertexAttributes() {
        material.getProgram().getAttributeLocations().forEach(location -> glDisableVertexAttribArray(location));
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Material getMaterial() {
        return material;
    }

    public Matrix4f getModel() {
        return getLocalToWorld();
    }

    private void setupBuffers() {
        float[] vertices = geometry.getVertices();
        float[] colors = geometry.getColors();

        ShaderProgram program = material.getProgram();

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
        verticesVbo = new FloatVertexBufferObject(verticesBuffer, VertexBufferObject.BUFFER_TARGET_ARRAY_BUFFER);
        int positionLocation = program.getAttributeLocation(program.positionAttributeName);
        verticesVbo.addVertexAttributePointer(new VertexAttributePointer(
                positionLocation,
                4,
                VertexAttributePointer.DATA_TYPE_FLOAT
        ));

        FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorsBuffer.put(colors);
        colorsBuffer.flip();
        colorsVbo = new FloatVertexBufferObject(colorsBuffer, VertexBufferObject.BUFFER_TARGET_ARRAY_BUFFER);
        int colorLocation = program.getAttributeLocation(program.colorAttributeName);
        colorsVbo.addVertexAttributePointer(new VertexAttributePointer(
                colorLocation,
                4,
                VertexAttributePointer.DATA_TYPE_FLOAT
        ));


        vao = new VertexArrayObject(geometry.isIndexed());

        vao.setVerticesVbo(verticesVbo);
        vao.setColorsVbo(colorsVbo);

        if (geometry.isIndexed()) {
            short[] indices = geometry.getIndices();
            ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(indices.length);
            indicesBuffer.put(indices);
            indicesBuffer.flip();
            indicesVbo = new ShortVertexBufferObject(indicesBuffer, VertexBufferObject.BUFFER_TARGET_ELEMENT_ARRAY_BUFFER);
            vao.setIndicesVbo(indicesVbo);
        }

        vao.sendVboDataAutoBind();
    }

    public void updateColors() {
        float[] vertices = geometry.getColors();
        FloatBuffer tempBuffer = BufferUtils.createFloatBuffer(vertices.length);
        tempBuffer.put(vertices);
        tempBuffer.flip();

        colorsVbo.setBufferData(tempBuffer);

        vao.bind();
        {
            colorsVbo.sendBufferDataAutoBind();
        }
        vao.unbind();
    }

    public void updateVertices() {
        float[] vertices = geometry.getVertices();
        FloatBuffer tempBuffer = BufferUtils.createFloatBuffer(vertices.length);
        tempBuffer.put(vertices);
        tempBuffer.flip();
        verticesVbo.setBufferData(tempBuffer);

        vao.bind();
        {
            verticesVbo.sendBufferDataAutoBind();
        }
        vao.unbind();
    }

    public VertexArrayObject getVao() {
        return vao;
    }

    public void destroy() {
        deleteVao();
        material.destroyShaderProgram();
    }

    private void deleteVao() {
        vao.bind();
        {
            disableVertexAttributes();
            deleteBuffers();
        }
        vao.unbind();
        vao.delete();
    }

    private void deleteBuffers() {
        verticesVbo.delete();
        colorsVbo.delete();
        if (geometry.isIndexed()) {
            indicesVbo.delete();
        }
    }

}
