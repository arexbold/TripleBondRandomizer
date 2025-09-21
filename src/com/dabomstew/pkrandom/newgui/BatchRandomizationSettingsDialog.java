package com.dabomstew.pkrandom.newgui;

/*----------------------------------------------------------------------------*/
/*--  BatchRandomizationSettingsDialog.java - a dialog for configuring      --*/
/*--                                          batch randomization settings  --*/
/*--                                                                        --*/
/*--  Part of "Universal Pokemon Randomizer ZX" by the UPR-ZX team          --*/
/*--  Originally part of "Universal Pokemon Randomizer" by Dabomstew        --*/
/*--  Pokemon and any associated names and the like are                     --*/
/*--  trademark and (C) Nintendo 1996-2020.                                 --*/
/*--                                                                        --*/
/*--  The custom code written here is licensed under the terms of the GPL:  --*/
/*--                                                                        --*/
/*--  This program is free software: you can redistribute it and/or modify  --*/
/*--  it under the terms of the GNU General Public License as published by  --*/
/*--  the Free Software Foundation, either version 3 of the License, or     --*/
/*--  (at your option) any later version.                                   --*/
/*--                                                                        --*/
/*--  This program is distributed in the hope that it will be useful,       --*/
/*--  but WITHOUT ANY WARRANTY; without even the implied warranty of        --*/
/*--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the          --*/
/*--  GNU General Public License for more details.                          --*/
/*--                                                                        --*/
/*--  You should have received a copy of the GNU General Public License     --*/
/*--  along with this program. If not, see <http://www.gnu.org/licenses/>.  --*/
/*----------------------------------------------------------------------------*/

import com.dabomstew.pkrandom.BatchRandomizationSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class BatchRandomizationSettingsDialog extends JDialog {
    private JPanel mainPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JCheckBox enableBatchRandomizationCheckBox;
    private JSpinner numberOfRandomizedROMsSpinner;
    private JSpinner startingIndexSpinner;
    private JTextField fileNamePrefixTextField;
    private JCheckBox generateLogFilesCheckBox;
    private JCheckBox autoAdvanceIndexCheckBox;
    private JButton chooseDirectoryButton;
    private JLabel outputDirectoryLabel;

    private JFileChooser outputDirectoryFileChooser;

    private final BatchRandomizationSettings currentSettings;

    public BatchRandomizationSettings getCurrentSettings() {
        return this.currentSettings;
    }

    public BatchRandomizationSettingsDialog(JFrame parent, BatchRandomizationSettings currentSettings) {
        super(parent, true);
        add(mainPanel);
        ResourceBundle bundle = ResourceBundle.getBundle("com/dabomstew/pkrandom/newgui/Bundle");
        setTitle(bundle.getString("BatchRandomizationSettingsDialog.title"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        getRootPane().setDefaultButton(okButton);

        this.currentSettings = currentSettings.clone();

        initializeControls();
        setLocationRelativeTo(parent);
        pack();
        setVisible(true);
    }

    private void onOK() {
        updateSettings();
        setVisible(false);
    }

    private void onCancel() {
        // add your code here if necessary
        setVisible(false);
    }

    private void initializeControls() {
        outputDirectoryFileChooser = new JFileChooser();
        okButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        mainPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        SpinnerNumberModel numberOfRandomizedROMsModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        numberOfRandomizedROMsSpinner.setModel(numberOfRandomizedROMsModel);

        SpinnerNumberModel startingIndexModel = new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1);
        startingIndexSpinner.setModel(startingIndexModel);

        chooseDirectoryButton.addActionListener(e -> {
            int selectionResult = outputDirectoryFileChooser.showDialog(this, "Select");
            if (selectionResult == JFileChooser.APPROVE_OPTION) {
                outputDirectoryFileChooser.setCurrentDirectory(outputDirectoryFileChooser.getSelectedFile());
                outputDirectoryLabel.setText(outputDirectoryFileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        setInitialControlValues();
        setControlsEnabled(currentSettings.isBatchRandomizationEnabled());
    }

    private void setInitialControlValues() {
        enableBatchRandomizationCheckBox.setSelected(currentSettings.isBatchRandomizationEnabled());
        generateLogFilesCheckBox.setSelected(currentSettings.shouldGenerateLogFile());
        autoAdvanceIndexCheckBox.setSelected(currentSettings.shouldAutoAdvanceStartingIndex());
        numberOfRandomizedROMsSpinner.setValue(currentSettings.getNumberOfRandomizedROMs());
        startingIndexSpinner.setValue(currentSettings.getStartingIndex());
        fileNamePrefixTextField.setText(currentSettings.getFileNamePrefix());
        outputDirectoryLabel.setText(currentSettings.getOutputDirectory());
        outputDirectoryFileChooser.setCurrentDirectory(new File(currentSettings.getOutputDirectory()));
        outputDirectoryFileChooser.setSelectedFile(new File(currentSettings.getOutputDirectory()));
        outputDirectoryFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        enableBatchRandomizationCheckBox.addActionListener(a -> setControlsEnabled(enableBatchRandomizationCheckBox.isSelected()));
    }

    private void setControlsEnabled(boolean enabled) {
        numberOfRandomizedROMsSpinner.setEnabled(enabled);
        startingIndexSpinner.setEnabled(enabled);
        fileNamePrefixTextField.setEnabled(enabled);
        generateLogFilesCheckBox.setEnabled(enabled);
        autoAdvanceIndexCheckBox.setEnabled(enabled);
        chooseDirectoryButton.setEnabled(enabled);
    }

    private void updateSettings() {
        currentSettings.setBatchRandomizationEnabled(enableBatchRandomizationCheckBox.isSelected());
        currentSettings.setGenerateLogFile(generateLogFilesCheckBox.isSelected());
        currentSettings.setAutoAdvanceStartingIndex(autoAdvanceIndexCheckBox.isSelected());
        currentSettings.setNumberOfRandomizedROMs((Integer) numberOfRandomizedROMsSpinner.getValue());
        currentSettings.setStartingIndex((Integer) startingIndexSpinner.getValue());
        currentSettings.setFileNamePrefix(fileNamePrefixTextField.getText());
        currentSettings.setOutputDirectory(outputDirectoryFileChooser.getSelectedFile().getAbsolutePath());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setMinimumSize(new Dimension(615, 250));
        mainPanel.setPreferredSize(new Dimension(615, 250));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setMaximumSize(new Dimension(200, 50));
        panel1.setMinimumSize(new Dimension(200, 50));
        panel1.setPreferredSize(new Dimension(200, 50));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel1, gbc);
        okButton = new JButton();
        okButton.setMaximumSize(new Dimension(100, 30));
        okButton.setMinimumSize(new Dimension(100, 30));
        okButton.setPreferredSize(new Dimension(100, 30));
        this.$$$loadButtonText$$$(okButton, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.okButton.text"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(okButton, gbc);
        cancelButton = new JButton();
        cancelButton.setMaximumSize(new Dimension(100, 30));
        cancelButton.setMinimumSize(new Dimension(100, 30));
        cancelButton.setOpaque(false);
        cancelButton.setPreferredSize(new Dimension(100, 30));
        this.$$$loadButtonText$$$(cancelButton, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.cancelButton.text"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        panel1.add(cancelButton, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer2, gbc);
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.startingIndexLabel.text"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(label1, gbc);
        fileNamePrefixTextField = new JTextField();
        fileNamePrefixTextField.setMargin(new Insets(2, 6, 2, 6));
        fileNamePrefixTextField.setMaximumSize(new Dimension(160, 30));
        fileNamePrefixTextField.setMinimumSize(new Dimension(160, 30));
        fileNamePrefixTextField.setPreferredSize(new Dimension(160, 30));
        fileNamePrefixTextField.setToolTipText(this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.fileNamePrefixTextBox.toolTipText"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(fileNamePrefixTextField, gbc);
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.fileNamePrefixLabel.text"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(label2, gbc);
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.numberOfRandomizedROMsLabel.text"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(label3, gbc);
        startingIndexSpinner = new JSpinner();
        startingIndexSpinner.setToolTipText(this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.startingIndexSpinner.toolTipText"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(startingIndexSpinner, gbc);
        numberOfRandomizedROMsSpinner = new JSpinner();
        numberOfRandomizedROMsSpinner.setToolTipText(this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.numberOfRandomizedROMsSpinner.toolTipText"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(numberOfRandomizedROMsSpinner, gbc);
        enableBatchRandomizationCheckBox = new JCheckBox();
        enableBatchRandomizationCheckBox.setAlignmentY(0.5f);
        this.$$$loadButtonText$$$(enableBatchRandomizationCheckBox, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.enableCheckBox.text"));
        enableBatchRandomizationCheckBox.setToolTipText(this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.enableCheckBox.toolTipText"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(enableBatchRandomizationCheckBox, gbc);
        chooseDirectoryButton = new JButton();
        chooseDirectoryButton.setAlignmentY(0.0f);
        chooseDirectoryButton.setInheritsPopupMenu(true);
        chooseDirectoryButton.setLabel("Output directory...");
        chooseDirectoryButton.setMaximumSize(new Dimension(165, 30));
        chooseDirectoryButton.setMinimumSize(new Dimension(165, 30));
        chooseDirectoryButton.setPreferredSize(new Dimension(165, 30));
        this.$$$loadButtonText$$$(chooseDirectoryButton, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.outputDirectoryButton.text"));
        chooseDirectoryButton.setToolTipText(this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.outputDirectoryButton.toolTipText"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(chooseDirectoryButton, gbc);
        outputDirectoryLabel = new JLabel();
        outputDirectoryLabel.setMaximumSize(new Dimension(350, 16));
        outputDirectoryLabel.setMinimumSize(new Dimension(350, 16));
        outputDirectoryLabel.setPreferredSize(new Dimension(350, 16));
        outputDirectoryLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(outputDirectoryLabel, gbc);
        generateLogFilesCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(generateLogFilesCheckBox, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.generateLogFilesCheckBox.text"));
        generateLogFilesCheckBox.setToolTipText(this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.generateLogFilesCheckBox.toolTipText"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(generateLogFilesCheckBox, gbc);
        autoAdvanceIndexCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(autoAdvanceIndexCheckBox, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.autoAdvanceIndexCheckBox.text"));
        autoAdvanceIndexCheckBox.setToolTipText(this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "BatchRandomizationSettingsDialog.autoAdvanceIndexCheckBox.toolTipText"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(autoAdvanceIndexCheckBox, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer3, gbc);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
