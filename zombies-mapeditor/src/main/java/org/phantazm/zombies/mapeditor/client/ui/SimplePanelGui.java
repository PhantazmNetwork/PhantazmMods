package org.phantazm.zombies.mapeditor.client.ui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;

/**
 * A very simple GUI that uses a {@link WGridPanel} as its root panel.
 */
public class SimplePanelGui extends LightweightGuiDescription {
    /**
     * The root {@link WGridPanel}, which is the same object as {@link LightweightGuiDescription#rootPanel}.
     */
    protected final WGridPanel gridPanelRoot;

    /**
     * The grid size for the root {@link WGridPanel}.
     */
    protected final int grid;

    /**
     * Creates a new SimplePanelGui with root panel measuring the given width and height.
     *
     * @param width  the width of the panel
     * @param height the height of the panel
     */
    public SimplePanelGui(int width, int height) {
        setRootPanel(gridPanelRoot = new WGridPanel(grid = 18));

        gridPanelRoot.setSize(width, height);
        gridPanelRoot.setInsets(Insets.ROOT_PANEL);
    }
}
