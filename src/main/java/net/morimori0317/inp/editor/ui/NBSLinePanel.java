package net.morimori0317.inp.editor.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.paint.LinePainter2D;
import com.intellij.util.ui.JBUI;
import dev.felnull.fnnbs.Layer;
import dev.felnull.fnnbs.NBS;
import dev.felnull.fnnbs.Note;
import net.morimori0317.inp.INPIcons;
import net.morimori0317.inp.player.NBSPlayerService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NBSLinePanel extends JPanel implements Disposable {
    private static final int NOTE_SIZE = 32;
    private final NBS nbs;
    private final NoteLinePanel noteLine;
    private final Project project;

    public NBSLinePanel(@NotNull Project project, NBS nbs) {
        super(new BorderLayout());
        this.nbs = nbs;
        this.project = project;

        int nwidth = NOTE_SIZE * nbs.getSongLength();
        int nheight = NOTE_SIZE * nbs.getLayers().size();

        JPanel timeLine = new TimeLinePanel();
        timeLine.setPreferredSize(JBUI.size(nwidth, 20));

        noteLine = new NoteLinePanel();
        noteLine.setPreferredSize(JBUI.size(nwidth, nheight));

        JPanel thePanel = new JPanel(new BorderLayout());
        thePanel.add(timeLine, BorderLayout.NORTH);
        thePanel.add(noteLine, BorderLayout.CENTER);

        add(new JBScrollPane(thePanel));
    }

    @Override
    public void dispose() {
        Disposer.dispose(noteLine);
    }

    private class TimeLinePanel extends JPanel {
        private TimeLinePanel() {
            setBackground(JBColor.BLUE);
        }
    }

    private class NoteLinePanel extends JPanel implements Disposable {
        private final List<NoteLabel> noteLabels = new ArrayList<>();

        private NoteLinePanel() {
            setLayout(null);

            for (int i = 0; i < nbs.getLayers().size(); i++) {
                Layer layer = nbs.getLayers().get(i);
                for (Map.Entry<Integer, Note> entry : layer.getNotes().entrySet()) {
                    NoteLabel notel = new NoteLabel(layer, entry.getValue());
                    notel.setBounds(entry.getKey() * NOTE_SIZE, i * NOTE_SIZE, NOTE_SIZE, NOTE_SIZE);
                    add(notel);
                    this.noteLabels.add(notel);
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

        @Override
        public void dispose() {
            for (NoteLabel noteLabel : noteLabels) {
                Disposer.dispose(noteLabel);
            }
        }
    }

    private class NoteLabel extends JBLabel implements MouseListener, Disposable {
        private final Layer layer;
        private final Note note;

        private NoteLabel(Layer layer, Note note) {
            super(INPIcons.getInstrumentIcon(note.getInstrument(nbs)));
            this.layer = layer;
            this.note = note;

            addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            NBSPlayerService.getInstance(project).play(nbs, layer, note, false);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void dispose() {
            removeMouseListener(this);
        }
    }
}
