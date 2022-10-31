package net.morimori0317.inp.editor.ui;

import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.paint.LinePainter2D;
import com.intellij.util.ui.JBUI;
import net.morimori0317.inp.INPIcons;
import net.morimori0317.inp.nbs.Layer;
import net.morimori0317.inp.nbs.NBS;
import net.morimori0317.inp.nbs.Note;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class NBSLinePanel extends JPanel {
    private static final int NOTE_SIZE = 32;
    private final NBS nbs;

    public NBSLinePanel(NBS nbs) {
        super(new BorderLayout());
        this.nbs = nbs;

        int nwidth = NOTE_SIZE * nbs.getSongLength();
        int nheight = NOTE_SIZE * nbs.getLayers().size();

        JPanel timeLine = new TimeLinePanel();
        timeLine.setPreferredSize(JBUI.size(nwidth, 20));

        JPanel noteLine = new NoteLinePanel();
        noteLine.setPreferredSize(JBUI.size(nwidth, nheight));

        JPanel thePanel = new JPanel(new BorderLayout());
        thePanel.add(timeLine, BorderLayout.NORTH);
        thePanel.add(noteLine, BorderLayout.CENTER);

        add(new JBScrollPane(thePanel));
    }

    private class TimeLinePanel extends JPanel {
        private TimeLinePanel() {
            setBackground(JBColor.BLUE);
        }
    }

    private class NoteLinePanel extends JPanel {
        private NoteLinePanel() {
            setLayout(null);

            for (int i = 0; i < nbs.getLayers().size(); i++) {
                Layer layer = nbs.getLayers().get(i);
                for (Map.Entry<Integer, Note> entry : layer.getNotes().entrySet()) {
                    NoteLabel notel = new NoteLabel();
                    notel.setBounds(entry.getKey() * NOTE_SIZE, i * NOTE_SIZE, NOTE_SIZE, NOTE_SIZE);
                    add(notel);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Color c1 = ColorUtil.mix(JBColor.DARK_GRAY, JBColor.WHITE, 0.75);
            Color c2 = ColorUtil.mix(JBColor.GRAY, JBColor.WHITE, 0.5);
            Color c3 = ColorUtil.mix(JBColor.DARK_GRAY, JBColor.WHITE, 0.5);

            for (int i = 1; i < getWidth() / NOTE_SIZE + 1; i++) {
                int lv = i % 4 != 0 ? 0 : i % 8 == 0 ? 2 : 1;
                g.setColor(lv == 0 ? c1 : lv == 1 ? c2 : c3);
                LinePainter2D.paint((Graphics2D) g, NOTE_SIZE * i, 0, NOTE_SIZE * i, getHeight(), LinePainter2D.StrokeType.INSIDE, lv == 0 ? 1 : 2);
            }
        }
    }

    private class NoteLabel extends JBLabel {
        private NoteLabel() {
            super(INPIcons.NOTE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(JBColor.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
