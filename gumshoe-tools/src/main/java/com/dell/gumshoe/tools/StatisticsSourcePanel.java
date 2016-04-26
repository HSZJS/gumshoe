package com.dell.gumshoe.tools;
import static com.dell.gumshoe.tools.Swing.flow;
import static com.dell.gumshoe.tools.Swing.groupButtons;

import com.dell.gumshoe.ProbeManager;
import com.dell.gumshoe.stack.Stack;
import com.dell.gumshoe.stats.StatisticAdder;
import com.dell.gumshoe.tools.graph.StackGraphPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Map;

public class StatisticsSourcePanel extends JPanel {
    static final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

    private final StackGraphPanel target;
    private final FileSourcePanel fileSource;
    private final ProbeSourcePanel probeSource;
    private final JPanel cardPanel = new JPanel();
    private final CardLayout sourceCardLayout = new CardLayout();
    private final JLabel status = new JLabel("No data currently displayed");

    public StatisticsSourcePanel(StackGraphPanel target, ProbeManager probe) {
        this.target = target;
        fileSource = new FileSourcePanel(this);
        probeSource = new ProbeSourcePanel(this, probe);

        final JRadioButton jvmButton = new JRadioButton("probe this JVM");
        jvmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sourceCardLayout.show(cardPanel, "jvm");
            }
        });
        final JRadioButton fileButton = new JRadioButton("text file");
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sourceCardLayout.show(cardPanel, "file");
            }
        });

        groupButtons(jvmButton, fileButton);
        final JPanel sourcePanel = flow(new JLabel("Source:"), jvmButton, fileButton);

        cardPanel.setLayout(sourceCardLayout);
        cardPanel.add(fileSource, "file");
        cardPanel.add(probeSource, "jvm");

        setLayout(new BorderLayout());
        add(sourcePanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        // if there is a main, that is the default source
        if(probe!=null) {
            jvmButton.setSelected(true);
            sourceCardLayout.show(cardPanel, "jvm");
        } else {
            jvmButton.setEnabled(false);
            fileButton.setSelected(true);
            sourceCardLayout.show(cardPanel, "file");
        }
    }

    public void setStatus(String message) {
        status.setText(message);
    }

    public void setSample(Map<Stack,StatisticAdder> stats) {
        target.updateModel(stats);
    }
}
