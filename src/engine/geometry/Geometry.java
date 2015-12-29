package engine.geometry;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Geometry {

    public static final int DRAW_MODE_TRIANGLES = GL_TRIANGLES;
    public static final int DRAW_MODE_POINTS = GL_POINTS;
    public static final int DRAW_MODE_LINES = GL_LINES;

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

    public void setDrawMode(int dm) {
        drawMode = dm;
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
