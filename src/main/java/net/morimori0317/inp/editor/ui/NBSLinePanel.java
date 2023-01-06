package net.morimori0317.inp.editor.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.SideBorder;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.paint.LinePainter2D;
import com.intellij.util.MathUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.update.UiNotifyConnector;
import dev.felnull.fnnbs.Layer;
import dev.felnull.fnnbs.NBS;
import dev.felnull.fnnbs.Note;
import dev.felnull.fnnbs.instrument.Instrument;
import net.morimori0317.inp.INPIcons;
import net.morimori0317.inp.player.NBSPlayer;
import net.morimori0317.inp.player.NBSPlayerService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NBSLinePanel extends JPanel implements Disposable {
    private static final Color BACKGROUND_COLOR = new JBColor(() -> EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());
    private static final String[] KEYS = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private static final int NOTE_SIZE = 32;
    private final NBS nbs;
    private final NoteLinePanel noteLine;
    private final Project project;
    private final NBSPlayer nbsPlayer;
    private final AtomicInteger lastSlideTick = new AtomicInteger();
    private final AtomicInteger slidePoint = new AtomicInteger();
    private final JBScrollPane noteScrollPane;

    public NBSLinePanel(@NotNull Project project, NBS nbs, NBSPlayer nbsPlayer) {
        super(new BorderLayout());
        this.nbs = nbs;
        this.project = project;
        this.nbsPlayer = nbsPlayer;

        int nwidth = NOTE_SIZE * nbs.getSongLength();
        int nheight = NOTE_SIZE * nbs.getLayers().size();

        JPanel timeLine = new TimeLinePanel();
        timeLine.setPreferredSize(JBUI.size(nwidth, 10));
        JBScrollPane timeScrollPane = new JBScrollPane(timeLine);
        timeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        timeScrollPane.setWheelScrollingEnabled(false);
        timeScrollPane.getHorizontalScrollBar().setEnabled(false);


        noteLine = new NoteLinePanel(nheight);
        noteLine.setPreferredSize(JBUI.size(nwidth, nheight));
        noteScrollPane = new JBScrollPane(noteLine);

        noteScrollPane.getViewport().addChangeListener(e -> {
            Point point = noteScrollPane.getViewport().getViewPosition();
            timeScrollPane.getViewport().setViewPosition(new Point(point.x, 0));

            if (nbsPlayer.isPlaying())
                noteScrollPane.getViewport().setViewPosition(new Point(slidePoint.get(), point.y));
        });

        JPanel thePanel = new JPanel(new BorderLayout());
        thePanel.add(timeScrollPane, BorderLayout.NORTH);
        thePanel.add(noteScrollPane, BorderLayout.CENTER);

        add(thePanel);

        UiNotifyConnector.doWhenFirstShown(noteLine, () -> {
            noteLine.playBarLabel.setXBound(0);
        });
    }

    protected void onNBSPlayProgress(float prograess) {
        noteLine.playBarLabel.setXBound((int) ((float) NOTE_SIZE * prograess));

        int slide = (int) (Math.floor(prograess) - lastSlideTick.get());
        if (Math.abs(slide * (float) NOTE_SIZE) >= getWidth()) {
            lastSlideTick.set((int) Math.floor(prograess));
            setSlidePoint(prograess, noteScrollPane);
        }

        Point point = noteScrollPane.getViewport().getViewPosition();
        noteScrollPane.getViewport().setViewPosition(new Point(slidePoint.get(), point.y));
    }

    protected void onNBSPlayChange(NBSPlayer.PlayState playState) {
            /*noteScrollPane.getHorizontalScrollBar().setEnabled(!ply);
            noteScrollPane.getHorizontalScrollBar().setVisible(!ply);*/

        if (playState == NBSPlayer.PlayState.PLAY) {
            Point point = noteScrollPane.getViewport().getViewPosition();
            float pointPar = (float) point.x / (float) noteScrollPane.getViewport().getViewSize().getWidth();

            lastSlideTick.set((int) Math.floor((float) nbs.getSongLength() * pointPar));
            int slide = (int) (Math.floor(nbsPlayer.getTick()) - lastSlideTick.get());
            slidePoint.set(point.x);
            if (Math.abs(slide * (float) NOTE_SIZE) >= getWidth())
                setSlidePoint(nbsPlayer.getTick(), noteScrollPane);

        } else if (playState == NBSPlayer.PlayState.STOP) {
            noteLine.playBarLabel.setXBound(0);
        }
    }

    private void setSlidePoint(float progress, JBScrollPane scrollPane) {
        float par = progress / (float) nbs.getSongLength();

        float pointPar = MathUtil.clamp(par, 0, 1);
        slidePoint.set((int) ((float) scrollPane.getViewport().getViewSize().getWidth() * pointPar));
    }

    @Override
    public void dispose() {
        Disposer.dispose(noteLine);
    }

    private class TimeLinePanel extends JPanel {
        private TimeLinePanel() {
            setBackground(BACKGROUND_COLOR);
            Color borderColor = ColorUtil.mix(JBColor.DARK_GRAY, JBColor.WHITE, 0.3);
            setBorder(IdeBorderFactory.createBorder(borderColor, SideBorder.BOTTOM));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

          /*  Color borderColor = ColorUtil.mix(JBColor.DARK_GRAY, JBColor.WHITE, 0.3);
            g.setColor(borderColor);
            LinePainter2D.paint((Graphics2D) g, 0, getHeight() - 2, getWidth(), getHeight() - 2, LinePainter2D.StrokeType.INSIDE, 2);*/

            Color lineColor = JBColor.DARK_GRAY;
            g.setColor(lineColor);

            for (int i = 1; i < getWidth() / NOTE_SIZE + 1; i++) {
                boolean ft = i % 4 == 0;

                LinePainter2D.paint((Graphics2D) g, (NOTE_SIZE * i), getHeight() - (ft ? 6 : 3), NOTE_SIZE * i, getHeight(), LinePainter2D.StrokeType.INSIDE, ft ? 2 : 1);
            }
        }
    }

    private class NoteLinePanel extends JPanel implements Disposable {
        private final List<NoteLabel> noteLabels = new ArrayList<>();
        private final PlayBarPanel playBarLabel = new PlayBarPanel();

        private NoteLinePanel(int noteHeight) {
            setLayout(null);

            add(playBarLabel);

            for (int i = 0; i < nbs.getLayers().size(); i++) {
                Layer layer = nbs.getLayers().get(i);
                for (Map.Entry<Integer, Note> entry : layer.getNotes().entrySet()) {
                    NoteLabel notel = new NoteLabel(layer, entry.getValue());
                    notel.setBounds(entry.getKey() * NOTE_SIZE, i * NOTE_SIZE, NOTE_SIZE, NOTE_SIZE);
                    add(notel);
                    this.noteLabels.add(notel);
                }
            }

            setBackground(BACKGROUND_COLOR);
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

            setLayout(new BorderLayout());

            JBLabel keyNameLabel = new JBLabel(KEYS[note.getKey() % 12], SwingConstants.CENTER);

            keyNameLabel.setForeground(new JBColor(() -> EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground()));
            add(keyNameLabel, BorderLayout.CENTER);


            if (!isMcRange())
                setBorder(IdeBorderFactory.createBorder(JBColor.RED));
        }

        private boolean isMcRange() {
            Instrument instrument = note.getInstrument(nbs);
            float dfkey = note.getInstrument(nbs).getDefaultPitch() - instrument.getDefaultPitch();
            float pitch = (float) Math.pow(2.0f, (double) (note.getKey() - instrument.getDefaultPitch() + dfkey) / 12.0f);
            return pitch >= 0.5f && pitch <= 2.0F;
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

    private class PlayBarPanel extends JPanel {
        private PlayBarPanel() {
            setBorder(IdeBorderFactory.createBorder(SideBorder.LEFT | SideBorder.RIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(JBColor.BLUE);
            LinePainter2D.paint((Graphics2D) g, 0, 0, 0, getHeight(), LinePainter2D.StrokeType.INSIDE, 5);
        }

        protected void setXBound(int x) {
            noteLine.playBarLabel.setBounds(x, 0, 3, noteLine.getHeight());
        }
    }
}
