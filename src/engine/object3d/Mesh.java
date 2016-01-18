package engine.object3d;

import engine.geometry.Geometry;
import engine.material.Material;
import engine.opengl.vao.VertexArrayObject;
import engine.opengl.vbo.*;
import engine.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

public class Mesh extends Object3d {

    //TODO: check destroy.

    private Geometry geometry;
    private Material material;
    private VertexArrayObject vao;

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
        ShaderProgram program = material.getProgram();

        vao = new VertexArrayObject(geometry.isIndexed());

        VertexBufferObject verticesVbo = new FloatVertexBufferObject(setupFloatBuffer(geometry.getVertices()));
        verticesVbo.addVertexAttributePointer(new VertexAttributePointer(
                program.getAttributeLocation(program.positionAttributeName),
                4,
                VertexAttributePointer.DATA_TYPE_FLOAT
        ));
        vao.setVerticesVbo(verticesVbo);

        VertexBufferObject colorsVbo = new FloatVertexBufferObject(setupFloatBuffer(geometry.getColors()));
        colorsVbo.addVertexAttributePointer(new VertexAttributePointer(
                program.getAttributeLocation(program.colorAttributeName),
                4,
                VertexAttributePointer.DATA_TYPE_FLOAT
        ));
        vao.setColorsVbo(colorsVbo);

        if (geometry.isIndexed()) {
            IndexBufferObject indicesVbo = new ShortIndexBufferObject(setupShortBuffer(geometry.getIndices()));
            vao.setIndicesVbo(indicesVbo);
        } else {
            VertexBufferObject normalsVbo = new FloatVertexBufferObject(setupFloatBuffer(geometry.getNormals()));
            normalsVbo.addVertexAttributePointer(new VertexAttributePointer(
                    program.getAttributeLocation(program.normalAttributeName),
                    4,
                    VertexAttributePointer.DATA_TYPE_FLOAT
            ));
            vao.setNormalsVbo(normalsVbo);
        }

        vao.sendVboDataAutoBind();
    }

    public void updateColors() {
        VertexBufferObject colorsVbo = vao.getColorsVbo();
        FloatBuffer buffer = setupFloatBuffer(geometry.getColors());
        colorsVbo.setBufferData(buffer);

        vao.bind();
        {
            colorsVbo.sendDataAutoBind();
        }
        vao.unbind();
    }

    public void updateVertices() {
        VertexBufferObject verticesVbo = vao.getVerticesVbo();
        FloatBuffer buffer = setupFloatBuffer(geometry.getVertices());
        verticesVbo.setBufferData(buffer);

        vao.bind();
        {
            verticesVbo.sendDataAutoBind();
        }
        vao.unbind();
    }

    public VertexArrayObject getVao() {
        return vao;
    }

    private FloatBuffer setupFloatBuffer(float[] array) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
        buffer.put(array); buffer.flip();
        return buffer;
    }

    private ShortBuffer setupShortBuffer(short[] array) {
        ShortBuffer buffer = BufferUtils.createShortBuffer(array.length);
        buffer.put(array); buffer.flip();
        return buffer;
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
        vao.getVerticesVbo().delete();
        vao.getColorsVbo().delete();
        if (geometry.isIndexed()) {
            vao.getIndicesVbo().delete();
        }
    }

}
