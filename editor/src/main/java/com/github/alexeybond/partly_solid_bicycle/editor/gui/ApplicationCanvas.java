package com.github.alexeybond.partly_solid_bicycle.editor.gui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

public class ApplicationCanvas extends JPanel {
    private LwjglAWTCanvas application;

    public ApplicationCanvas(ApplicationListener listener, LwjglApplicationConfiguration configuration) {

        application = new LwjglAWTCanvas(listener, configuration);
        addApplicationCanvas();

        setMinimumSize(new Dimension(configuration.width, configuration.height));
    }

    private void addApplicationCanvas() {
        setLayout(new GridLayoutManager(1, 1));
        GridConstraints constraints = new GridConstraints();
        constraints.setFill(GridConstraints.FILL_BOTH);
        add(application.getCanvas(), constraints);
    }
}
