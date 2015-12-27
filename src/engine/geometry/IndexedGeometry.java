package engine.geometry;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class IndexedGeometry extends Geometry {

    private short[] indices;

    public IndexedGeometry(float[] vertices, float[] colors, short[] indices, int glDrawMode) {
        super(vertices, colors, glDrawMode);
        this.indices = indices;
        indexed = true;
    }

    public IndexedGeometry(float[] vertices, float[] colors, short[] indices) {
        this(vertices, colors, indices, GL_TRIANGLES);
    }

    public short[] getIndices() {
        return indices;
    }

}
