package engine.object3d;

import engine.geometry.Geometry;
import engine.material.Material;
import engine.opengl.VertexArrayObject;
import engine.opengl.VertexBufferObject;
import engine.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Mesh extends Object3d {

    //TODO: check destroy.

    Geometry geometry;
    Material material;

    private VertexArrayObject vao;

    private VertexBufferObject verticesVbo;
    private VertexBufferObject colorsVbo;
    private VertexBufferObject indicesVbo;

    private int verticesCount;
    private int colorsCount;
    private int indicesCount;

    public Mesh(Geometry g, Material m) {
        geometry = g;
        material = m;
        setupBuffers();
    }

    public void disableVertexAttributes() {
        material.getProgram().getAttributeLocations().forEach(location -> glDisableVertexAttribArray(location));
    }

    public int getColorsCount() {
        return colorsCount;
    }

    public int getColorsVboID() {
        return colorsVbo.getId();
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public int getIndicesCount() {
        return indicesCount;
    }

    public int getIndicesVboID() {
        return indicesVbo.getId();
    }

    public Material getMaterial() {
        return material;
    }

    public Matrix4f getModel() {
        return getLocalToWorld();
    }

    public int getVaoID() {
        return vao.getId();
    }

    public int getVerticesCount() {
        return verticesCount;
    }

    public int getVerticesVboID() {
        return verticesVbo.getId();
    }

    public boolean isIndexed() {
        return geometry.isIndexed();
    }

    private void setupBuffers() {
        float[] vertices = geometry.getVertices();
        float[] colors = geometry.getColors();
        short[] indices = geometry.getIndices();

        ShaderProgram program = material.getProgram();

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
        verticesCount = vertices.length;
        int positionLocation = program.getAttributeLocation(program.positionAttributeName);

        FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorsBuffer.put(colors);
        colorsBuffer.flip();
        colorsCount = colors.length;
        int colorLocation = program.getAttributeLocation(program.colorAttributeName);

        vao = new VertexArrayObject();
        vao.bind();
        {
            verticesVbo = new VertexBufferObject(VertexBufferObject.BUFFER_TARGET_ARRAY_BUFFER);
            verticesVbo.bind();
            {
                glBufferData(verticesVbo.getBufferTarget(), verticesBuffer, verticesVbo.getUsageHint());
                glVertexAttribPointer(positionLocation, 4, GL_FLOAT, false, 0, 0);
            }
            verticesVbo.unbind();

            colorsVbo = new VertexBufferObject(VertexBufferObject.BUFFER_TARGET_ARRAY_BUFFER);
            colorsVbo.bind();
            {
                glBufferData(colorsVbo.getBufferTarget(), colorsBuffer, colorsVbo.getUsageHint());
                glVertexAttribPointer(colorLocation, 4, GL_FLOAT, false, 0, 0);
            }
            verticesVbo.unbind();
        }
        vao.unbind();

        if (geometry.isIndexed()) {
            ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(indices.length);
            indicesBuffer.put(indices);
            indicesBuffer.flip();
            indicesCount = indices.length;

            indicesVbo = new VertexBufferObject(VertexBufferObject.BUFFER_TARGET_ELEMENT_ARRAY_BUFFER);
            indicesVbo.bind();
            {
                glBufferData(indicesVbo.getBufferTarget(), indicesBuffer, indicesVbo.getUsageHint());
            }
            indicesVbo.unbind();
        } else {
            indicesCount = 0;
        }
    }

    public void updateColors() {
        float[] vertices = geometry.getColors();
        FloatBuffer tempBuffer = BufferUtils.createFloatBuffer(vertices.length);
        tempBuffer.put(vertices);
        tempBuffer.flip();

        vao.bind();
        {
            colorsVbo.bind();
            {
                glBufferData(colorsVbo.getBufferTarget(), tempBuffer, colorsVbo.getUsageHint());
            }
            colorsVbo.unbind();
        }
        vao.unbind();
    }

    public void updateVertices() {
        float[] vertices = geometry.getVertices();
        FloatBuffer tempBuffer = BufferUtils.createFloatBuffer(vertices.length);
        tempBuffer.put(vertices);
        tempBuffer.flip();

        vao.bind();
        {
            verticesVbo.bind();
            {
                glBufferData(verticesVbo.getBufferTarget(), tempBuffer, verticesVbo.getUsageHint());
            }
            verticesVbo.unbind();
        }
        vao.unbind();
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
        verticesVbo.deleteBuffer();
        colorsVbo.deleteBuffer();
        if (geometry.isIndexed()) {
            indicesVbo.deleteBuffer();
        }
    }

}
