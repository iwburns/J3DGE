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
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

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

    private void bindVao() {
        glBindVertexArray(vao.getId());
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
        glBindVertexArray(vao.getId());
        {
            verticesVbo = new VertexBufferObject();
            glBindBuffer(GL_ARRAY_BUFFER, verticesVbo.getId());
            {
                glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);
                glVertexAttribPointer(positionLocation, 4, GL_FLOAT, false, 0, 0);
            }
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            colorsVbo = new VertexBufferObject();
            glBindBuffer(GL_ARRAY_BUFFER, colorsVbo.getId());
            {
                glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_DYNAMIC_DRAW);
                glVertexAttribPointer(colorLocation, 4, GL_FLOAT, false, 0, 0);
            }
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        glBindVertexArray(0);

        if (geometry.isIndexed()) {
            ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(indices.length);
            indicesBuffer.put(indices);
            indicesBuffer.flip();
            indicesCount = indices.length;

            indicesVbo = new VertexBufferObject();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVbo.getId());
            {
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_DYNAMIC_DRAW);
            }
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        } else {
            indicesCount = 0;
        }
    }

    private void unbindVao() {
        glBindVertexArray(0);
    }

    public void updateColors() {
        float[] vertices = geometry.getColors();
        FloatBuffer tempBuffer = BufferUtils.createFloatBuffer(vertices.length);
        tempBuffer.put(vertices);
        tempBuffer.flip();

        glBindVertexArray(vao.getId());
        {
            glBindBuffer(GL_ARRAY_BUFFER, colorsVbo.getId());
            {
                glBufferData(GL_ARRAY_BUFFER, tempBuffer, GL_DYNAMIC_DRAW);
            }
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        glBindVertexArray(0);
    }

    public void updateVertices() {
        float[] vertices = geometry.getVertices();
        FloatBuffer tempBuffer = BufferUtils.createFloatBuffer(vertices.length);
        tempBuffer.put(vertices);
        tempBuffer.flip();

        glBindVertexArray(vao.getId());
        {
            glBindBuffer(GL_ARRAY_BUFFER, verticesVbo.getId());
            {
                glBufferData(GL_ARRAY_BUFFER, tempBuffer, GL_DYNAMIC_DRAW);
            }
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        glBindVertexArray(0);
    }

    public void destroy() {
        deleteVao();
        material.destroyShaderProgram();
    }

    private void deleteVao() {
        bindVao();
        {
            disableVertexAttributes();
            deleteBuffers();
        }
        unbindVao();
        glDeleteVertexArrays(vao.getId());
    }

    private void deleteBuffers() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(verticesVbo.getId());
        glDeleteBuffers(colorsVbo.getId());
        if (geometry.isIndexed()) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glDeleteBuffers(indicesVbo.getId());
        }
    }

}
