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

    public void sendVertexAttributes(boolean autoBind) {
        if (autoBind) {
            bind();
        }
        vertexAttributePointers.forEach(vertexAttributePointer -> {
            glVertexAttribPointer(
                    vertexAttributePointer.getLocation(),
                    vertexAttributePointer.getSize(),
                    vertexAttributePointer.getType(),
                    vertexAttributePointer.isNormalized(),
                    vertexAttributePointer.getStride(),
                    vertexAttributePointer.getOffset()
            );
        });
        if (autoBind) {
            unbind();
        }
    }

    public void sendAllData() {
        bind();
        {
            sendBufferData(false);
            sendVertexAttributes(false);
        }
        unbind();
    }

    public abstract void sendBufferData(boolean autoBind);

    public abstract Buffer getBufferData();

    public abstract void setBufferData(Buffer b);

}
