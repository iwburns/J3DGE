package engine.opengl.vbo;

import java.nio.Buffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public abstract class VertexBufferObject {

    public static int BUFFER_TARGET_ARRAY_BUFFER = GL_ARRAY_BUFFER;
    public static int BUFFER_TARGET_ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

    public static int USAGE_HINT_STATIC_DRAW = GL_STATIC_DRAW;
    public static int USAGE_HINT_DYNAMIC_DRAW = GL_DYNAMIC_DRAW;

    protected int id;
    protected int bufferTarget;
    protected int usageHint;

    protected ArrayList<VertexAttributePointer> vertexAttributePointers = new ArrayList<>();

    public void bind() {
        glBindBuffer(bufferTarget, id);
    }

    public void unbind() {
        glBindBuffer(bufferTarget, 0);
    }

    public void delete() {
        unbind();
        glDeleteBuffers(id);
    }

    public int getId() {
        return id;
    }

    public int getBufferTarget() {
        return bufferTarget;
    }

    public int getUsageHint() {
        return usageHint;
    }

    public void setUsageHint(int usageHint) {
        this.usageHint = usageHint;
    }

    public void addVertexAttributePointer(VertexAttributePointer vertexAttributePointer) {
        vertexAttributePointers.add(vertexAttributePointer);
    }

    public void removeVertexAttributePointer(VertexAttributePointer vertexAttributePointer) {
        vertexAttributePointers.remove(vertexAttributePointer);
    }

    public void sendBufferDataAutoBind() {
        bind();
        sendBufferData();
        unbind();
    }

    public void sendVertexAttributesAutoBind() {
        bind();
        sendVertexAttributes();
        unbind();
    }

    public void sendVertexAttributes() {
        vertexAttributePointers.forEach(pointer -> {
            glVertexAttribPointer(
                    pointer.getLocation(),
                    pointer.getSize(),
                    pointer.getType(),
                    pointer.isNormalized(),
                    pointer.getStride(),
                    pointer.getOffset()
            );
        });
    }

    public void sendAllDataAutoBind() {
        bind();
        sendAllData();
        unbind();
    }

    public void sendAllData() {
        sendBufferData();
        sendVertexAttributes();
    }

    public abstract void sendBufferData();

    public abstract Buffer getBufferData();

    public abstract void setBufferData(Buffer b);

    public abstract int getSize();

}
