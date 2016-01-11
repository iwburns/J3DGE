package engine.geometry;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class IndexedGeometry extends Geometry {

    private short[] indices;

    public IndexedGeometry(float[] vertices, float[] normals, float[] colors, short[] indices, int glDrawMode) {
        super(vertices, normals, colors, glDrawMode);
        this.indices = indices;
        indexed = true;
    }

    public IndexedGeometry(float[] vertices, float[] normals, float[] colors, short[] indices) {
        this(vertices, normals, colors, indices, GL_TRIANGLES);
    }

    public short[] getIndices() {
        return indices;
    }

}
