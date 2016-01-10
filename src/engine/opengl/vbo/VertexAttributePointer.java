package engine.opengl.vbo;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class VertexAttributePointer {

    //TODO: add the other valid types
    public static final int DATA_TYPE_FLOAT = GL11.GL_FLOAT;

    private int location;
    private int size;
    private int type;
    private boolean normalized;
    private int stride;
    private long offset;


    //TODO: possibly add validation for things like size (1, 2, 3, or 4 only) etc.
    public VertexAttributePointer(int location, int size, int type) {
        this(location, size, type, false, 0, 0);
    }

    public VertexAttributePointer(int location, int size, int type, boolean normalized, int stride, long offset) {
        this.location = location;
        this.size = size;
        this.type = type;
        this.normalized = normalized;
        this.stride = stride;
        this.offset = offset;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }

    public int getStride() {
        return stride;
    }

    public void setStride(int stride) {
        this.stride = stride;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
