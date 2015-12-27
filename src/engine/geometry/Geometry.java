package engine.geometry;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Geometry {

    protected boolean indexed = false;
    protected float[] vertices;
    protected float[] colors;
    protected int drawMode;

    public Geometry(float[] vertices, float[] colors, int glDrawMode) {
        this.vertices = vertices;
        this.colors = colors;
        drawMode = glDrawMode;
    }

    public Geometry(float[] vertices, float[] colors) {
        this(vertices, colors, GL_TRIANGLES);
    }

    public float[] getColors() {
        return colors;
    }

    public int getDrawMode() {
        return drawMode;
    }

    public float[] getVertices() {
        return vertices;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public short[] getIndices() {
        return null;
    }

}
