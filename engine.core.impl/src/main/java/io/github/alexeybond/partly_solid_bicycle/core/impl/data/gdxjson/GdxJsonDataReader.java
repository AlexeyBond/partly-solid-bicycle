package io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.Queue;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;

import java.io.InputStream;

/**
 * Parses JSON documents to trees of {@link InputDataObject}'s.
 *
 * <p>
 *  This implementation is based on LibGDX JSON reader.
 *  The only good point about this implementation is that it works.
 * </p>
 *
 * TODO:: Rewrite without LibGDX JsonReader
 */
public class GdxJsonDataReader extends JsonReader {
    private final Queue<InputNode> nodeStack = new Queue<InputNode>();
    private InputNode curNode;

    private InputNode addChild(String name, InputNode node) {
        if (null == name) {
            curNode.addItem(node);
        } else {
            curNode.putField(name, node);
        }

        return node;
    }

    private void push(InputNode node) {
        nodeStack.addLast(curNode);
        curNode = node;
    }

    @Override
    protected void bool(String name, boolean value) {
        addChild(name, InputNode.newBoolean(value));
    }

    @Override
    protected void number(String name, long value, String stringValue) {
        addChild(name, InputNode.newNumber(value));
    }

    @Override
    protected void number(String name, double value, String stringValue) {
        addChild(name, InputNode.newNumber(value));
    }

    @Override
    protected void pop() {
        curNode = nodeStack.removeLast();
    }

    @Override
    protected void string(String name, String value) {
        addChild(name, InputNode.newString(value));
    }

    @Override
    protected void startArray(String name) {
        push(addChild(name, InputNode.newArray()));
    }

    @Override
    protected void startObject(String name) {
        push(addChild(name, InputNode.newObject()));
    }

    private InputNode.RootNode prepare() {
        InputNode.RootNode rootNode = new InputNode.RootNode();
        curNode = rootNode;
        nodeStack.clear();
        return rootNode;
    }

    public InputDataObject parseData(String string) {
        InputNode.RootNode rootNode = prepare();
        parse(string);
        return rootNode.getChild();
    }

    public InputDataObject parseData(FileHandle file) {
        InputNode.RootNode rootNode = prepare();
        parse(file);
        return rootNode.getChild();
    }

    public InputDataObject parseData(InputStream is) {
        InputNode.RootNode rootNode = prepare();
        parse(is);
        return rootNode.getChild();
    }
}
