package engine.geometry;

import static org.lwjgl.opengl.GL11.*;

public class Geometry {

    public static final int DRAW_MODE_TRIANGLES = GL_TRIANGLES;
    public static final int DRAW_MODE_POINTS = GL_POINTS;
    public static final int DRAW_MODE_LINES = GL_LINES;

    protected boolean indexed = false;
    protected float[] vertices;
    protected float[] normals;
    protected float[] colors;
    protected int drawMode;

    public Geometry(float[] vertices, float[] normals, float[] colors, int glDrawMode) {
        this.vertices = vertices;
        this.normals = normals;
        this.colors = colors;
        drawMode = glDrawMode;
    }

    public Geometry(float[] vertices, float[] normals, float[] colors) {
        this(vertices, normals, colors, DRAW_MODE_TRIANGLES);
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getColors() {
        return colors;
    }

    public short[] getIndices() {
        return null;
    }

    public int getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(int dm) {
        drawMode = dm;
    }

    public boolean isIndexed() {
        return indexed;
    }

}
