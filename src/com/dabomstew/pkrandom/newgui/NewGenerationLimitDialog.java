package com.dabomstew.pkrandom.newgui;

/*----------------------------------------------------------------------------*/
/*--  NewGenerationLimitDialog.java - a GUI interface to allow users to     --*/
/*--                                  limit which Pokemon appear based on   --*/
/*--                                  their generation of origin.           --*/
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

import com.dabomstew.pkrandom.pokemon.GenRestrictions;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewGenerationLimitDialog extends JDialog {
    private JCheckBox gen1CheckBox;
    private JCheckBox gen2CheckBox;
    private JCheckBox gen3CheckBox;
    private JCheckBox gen4CheckBox;
    private JCheckBox gen5CheckBox;
    private JCheckBox gen6CheckBox;
    private JCheckBox gen7CheckBox;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel mainPanel;
    private JLabel xyWarningLabel;
    private JCheckBox allowEvolutionaryRelativesCheckBox;

    private boolean pressedOk;
    private boolean isXY;

    public NewGenerationLimitDialog(JFrame parent, GenRestrictions current, int generation, boolean isXY) {
        super(parent, true);
        add(mainPanel);
        this.isXY = isXY;
        initComponents();
        initialState(generation);
        if (current != null) {
            current.limitToGen(generation);
            restoreFrom(current);
        }
        enableAndDisableBoxes();
        pressedOk = false;
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public boolean pressedOK() {
        return pressedOk;
    }

    public GenRestrictions getChoice() {
        GenRestrictions gr = new GenRestrictions();
        gr.allow_gen1 = gen1CheckBox.isSelected();
        gr.allow_gen2 = gen2CheckBox.isSelected();
        gr.allow_gen3 = gen3CheckBox.isSelected();
        gr.allow_gen4 = gen4CheckBox.isSelected();
        gr.allow_gen5 = gen5CheckBox.isSelected();
        gr.allow_gen6 = gen6CheckBox.isSelected();
        gr.allow_gen7 = gen7CheckBox.isSelected();
        gr.allow_evolutionary_relatives = allowEvolutionaryRelativesCheckBox.isSelected();
        return gr;
    }

    private void initialState(int generation) {
        if (generation < 2) {
            gen2CheckBox.setVisible(false);
        }
        if (generation < 3) {
            gen3CheckBox.setVisible(false);
        }
        if (generation < 4) {
            gen4CheckBox.setVisible(false);
        }
        if (generation < 5) {
            gen5CheckBox.setVisible(false);
        }
        if (generation < 6) {
            gen6CheckBox.setVisible(false);
        }
        if (generation < 7) {
            gen7CheckBox.setVisible(false);
        }

        allowEvolutionaryRelativesCheckBox.setEnabled(false);
        allowEvolutionaryRelativesCheckBox.setSelected(false);
    }

    private void restoreFrom(GenRestrictions restrict) {
        gen1CheckBox.setSelected(restrict.allow_gen1);
        gen2CheckBox.setSelected(restrict.allow_gen2);
        gen3CheckBox.setSelected(restrict.allow_gen3);
        gen4CheckBox.setSelected(restrict.allow_gen4);
        gen5CheckBox.setSelected(restrict.allow_gen5);
        gen6CheckBox.setSelected(restrict.allow_gen6);
        gen7CheckBox.setSelected(restrict.allow_gen7);
        allowEvolutionaryRelativesCheckBox.setSelected(restrict.allow_evolutionary_relatives);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ResourceBundle bundle = ResourceBundle.getBundle("com/dabomstew/pkrandom/newgui/Bundle");
        setTitle(bundle.getString("GenerationLimitDialog.title"));
        gen1CheckBox.addActionListener(ev -> enableAndDisableBoxes());
        gen2CheckBox.addActionListener(ev -> enableAndDisableBoxes());
        gen3CheckBox.addActionListener(ev -> enableAndDisableBoxes());
        gen4CheckBox.addActionListener(ev -> enableAndDisableBoxes());
        gen5CheckBox.addActionListener(ev -> enableAndDisableBoxes());
        gen6CheckBox.addActionListener(ev -> enableAndDisableBoxes());
        gen7CheckBox.addActionListener(ev -> enableAndDisableBoxes());
        allowEvolutionaryRelativesCheckBox.addActionListener(ev -> enableAndDisableBoxes());
        okButton.addActionListener(evt -> okButtonActionPerformed());
        cancelButton.addActionListener(evt -> cancelButtonActionPerformed());
        xyWarningLabel.setVisible(isXY);
        if (isXY) {
            okButton.setEnabled(false);
        }
        pack();
    }

    private void enableAndDisableBoxes() {
        // To prevent softlocks on the Successor Korrina fight, only turn
        // on the OK button for XY if at least one of Gens 1-4 is selected.
        if (isXY) {
            if (gen1CheckBox.isSelected() || gen2CheckBox.isSelected() || gen3CheckBox.isSelected() || gen4CheckBox.isSelected()) {
                okButton.setEnabled(true);
            } else {
                okButton.setEnabled(false);
            }
        }

        if (gen1CheckBox.isSelected() || gen2CheckBox.isSelected() || gen3CheckBox.isSelected() ||
                gen4CheckBox.isSelected() || gen5CheckBox.isSelected() || gen6CheckBox.isSelected() ||
                gen7CheckBox.isSelected()) {
            allowEvolutionaryRelativesCheckBox.setEnabled(true);
        } else {
            allowEvolutionaryRelativesCheckBox.setEnabled(false);
            allowEvolutionaryRelativesCheckBox.setSelected(false);
        }
    }

    private void okButtonActionPerformed() {
        pressedOk = true;
        setVisible(false);
    }

    private void cancelButtonActionPerformed() {
        pressedOk = false;
        setVisible(false);
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(cancelButton, gbc);
        okButton = new JButton();
        okButton.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(okButton, gbc);
        gen1CheckBox = new JCheckBox();
        gen1CheckBox.setText("Generation 1");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(gen1CheckBox, gbc);
        gen2CheckBox = new JCheckBox();
        gen2CheckBox.setText("Generation 2");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(gen2CheckBox, gbc);
        gen3CheckBox = new JCheckBox();
        gen3CheckBox.setText("Generation 3");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(gen3CheckBox, gbc);
        gen4CheckBox = new JCheckBox();
        gen4CheckBox.setText("Generation 4");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(gen4CheckBox, gbc);
        gen5CheckBox = new JCheckBox();
        gen5CheckBox.setText("Generation 5");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(gen5CheckBox, gbc);
        gen6CheckBox = new JCheckBox();
        gen6CheckBox.setText("Generation 6");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(gen6CheckBox, gbc);
        gen7CheckBox = new JCheckBox();
        gen7CheckBox.setText("Generation 7");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(gen7CheckBox, gbc);
        xyWarningLabel = new JLabel();
        Font xyWarningLabelFont = this.$$$getFont$$$(null, Font.BOLD, -1, xyWarningLabel.getFont());
        if (xyWarningLabelFont != null) xyWarningLabel.setFont(xyWarningLabelFont);
        this.$$$loadLabelText$$$(xyWarningLabel, this.$$$getMessageFromBundle$$$("com/dabomstew/pkrandom/newgui/Bundle", "GenerationLimitDialog.warningXYLabel.text"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 11;
        mainPanel.add(xyWarningLabel, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer4, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Include Pokemon from:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label1, gbc);
        allowEvolutionaryRelativesCheckBox = new JCheckBox();
        allowEvolutionaryRelativesCheckBox.setText("Allow Evolutionary Relatives");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(allowEvolutionaryRelativesCheckBox, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
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
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
