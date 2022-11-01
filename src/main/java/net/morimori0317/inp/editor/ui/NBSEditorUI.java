package net.morimori0317.inp.editor.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.panels.NonOpaquePanel;
import net.morimori0317.inp.editor.actions.NBSEditorActions;
import net.morimori0317.inp.nbs.NBSLoadResult;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class NBSEditorUI extends JPanel implements Disposable {
    private final NBSLoadResult nbsLoadResult;

    public NBSEditorUI(@NotNull Project project, @NotNull NBSLoadResult nbsLoadResult) {
        this.nbsLoadResult = nbsLoadResult;

        setLayout(new BorderLayout());

        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup toolbarActions = (ActionGroup) actionManager.getAction(NBSEditorActions.GROUP_TOOLBAR);
        ActionToolbar actionToolbar = actionManager.createActionToolbar(NBSEditorActions.ACTION_PLACE, toolbarActions, true);
        actionToolbar.setTargetComponent(this);

        JComponent toolbarPanel = actionToolbar.getComponent();

        JPanel topPanel = new NonOpaquePanel(new BorderLayout());
        topPanel.add(toolbarPanel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        if (nbsLoadResult.getNBS() != null) {
            add(new NBSLinePanel(project, nbsLoadResult.getNBS()), BorderLayout.CENTER);
        }
    }

    @Override
    public void dispose() {

    }
}
