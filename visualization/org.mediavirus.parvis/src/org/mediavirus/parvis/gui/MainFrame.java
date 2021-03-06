/*

 Copyright (c) 2001, 2002, 2003 Flo Ledermann <flo@subnet.at>

 This file is part of parvis - a parallel coordiante based data visualisation
 tool written in java. You find parvis and additional information on its
 website at http://www.mediavirus.org/parvis.

 parvis is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 parvis is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with parvis (in the file LICENSE.txt); if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

/*
 * Modified by:
 * @author Ketan Mane <kmane@indiana.edu> 
 */

package org.mediavirus.parvis.gui;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;

import org.mediavirus.parvis.file.STFFile;
import org.mediavirus.parvis.model.Brush;

/**
 * @author flo Modified by :
 * @author Ketan
 */
public class MainFrame extends JFrame implements ProgressListener,
		BrushListener {

	private static MainFrame INSTANCE = new MainFrame();

	public static MainFrame getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainFrame();
		}
		return INSTANCE;
	}
	
	
	/** Creates new form MainFrame */
	public MainFrame() {

		initComponents();
		parallelDisplay.addProgressListener(this);
		parallelDisplay.addBrushListener(this);

		BrushList brushList = new BrushList(parallelDisplay);
		brushList.setLocation(this.getX() + this.getWidth(), this.getY());
		brushList.show();

		CorrelationFrame correlationFrame = new CorrelationFrame(
				parallelDisplay);
		correlationFrame.setLocation(this.getX() + this.getWidth(), this.getY()
				+ brushList.getHeight());
		correlationFrame.show();
		this.setSize(800, 500);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		menuEditGroup = new javax.swing.ButtonGroup();
		buttonEditGroup = new javax.swing.ButtonGroup();
		statusPanel = new javax.swing.JPanel();
		progressPanel = new javax.swing.JPanel();
		progressLabel = new javax.swing.JLabel();
		progressBar = new javax.swing.JProgressBar();
		timeLabel = new javax.swing.JLabel();
		quickPrefPanel = new javax.swing.JPanel();
		axisLabelBox = new JCheckBox();
		histogramBox = new javax.swing.JCheckBox();
		zonesBox = new javax.swing.JCheckBox();
		tooltipBox = new javax.swing.JCheckBox();
		hoverBox = new javax.swing.JCheckBox();
		fuzzyBrushBox = new javax.swing.JCheckBox();
		radiusField = new javax.swing.JTextField();
		toolbarPanel = new javax.swing.JPanel();
		modeBar = new javax.swing.JToolBar();
		modeLabel = new javax.swing.JLabel();
		recCountLabel = new javax.swing.JLabel();
		displayLabel = new javax.swing.JLabel();
		orderButton = new javax.swing.JToggleButton();
		scaleButton = new javax.swing.JToggleButton();
		translateButton = new javax.swing.JToggleButton();
		brushButton = new javax.swing.JToggleButton();
		jSeparator2 = new javax.swing.JSeparator();
		jSeparator3 = new javax.swing.JSeparator();
		jSeparator4 = new javax.swing.JSeparator();
		jSeparator5 = new javax.swing.JSeparator();
		countLabel = new javax.swing.JLabel();
		resetBrushButton = new javax.swing.JButton();
		resetAllButton = new javax.swing.JButton();
		urlBar = new javax.swing.JToolBar();
		datasourceLabel = new javax.swing.JLabel();
		urlField = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		parallelDisplay = new org.mediavirus.parvis.gui.ParallelDisplay();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openMenu = new javax.swing.JMenuItem();
		saveBrushItem = new javax.swing.JMenuItem();
		editMenu = new javax.swing.JMenu();
		orderMenu = new javax.swing.JRadioButtonMenuItem();
		scaleMenu = new javax.swing.JRadioButtonMenuItem();
		translateMenu = new javax.swing.JRadioButtonMenuItem();
		brushMenu = new javax.swing.JRadioButtonMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		preferencesMenu = new javax.swing.JMenuItem();
		viewMenu = new javax.swing.JMenu();
		scaleZeroMaxItem = new javax.swing.JMenuItem();
		scaleMinMaxItem = new javax.swing.JMenuItem();
		scaleMinMaxAbsItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		helpItem = new javax.swing.JMenuItem();
		aboutItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Parallel Coordinate View");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		statusPanel.setLayout(new java.awt.BorderLayout());
		statusPanel.setBorder(new javax.swing.border.TitledBorder(
				new javax.swing.border.EtchedBorder(), "status",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Dialog", 0, 10)));
		statusPanel.setFont(new java.awt.Font("Dialog", 0, 10));
		statusPanel.setPreferredSize(new java.awt.Dimension(272, 50));

		progressPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.CENTER, 5, 0));

		progressLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		progressLabel.setText("progress:");
		progressPanel.add(progressLabel);

		progressBar.setFont(new java.awt.Font("Dialog", 0, 10));
		progressBar.setMaximumSize(new java.awt.Dimension(32767, 18));
		progressBar.setMinimumSize(new java.awt.Dimension(10, 16));
		progressBar.setPreferredSize(new java.awt.Dimension(100, 18));
		progressBar.setStringPainted(true);
		progressPanel.add(progressBar);

		timeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		timeLabel.setText("(0.0 s)");
		progressPanel.add(timeLabel);

		statusPanel.add(progressPanel, java.awt.BorderLayout.WEST);

		quickPrefPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.CENTER, 5, 0));

		// Toolbar Panel and its contents
		toolbarPanel.setLayout(new java.awt.GridLayout(2, 0));

		modeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		modeLabel.setText("Edit Mode: ");
		modeBar.add(modeLabel);

		orderButton.setFont(new java.awt.Font("Dialog", 0, 10));
		orderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/org/mediavirus/parvis/gui/reorder.gif")));
		orderButton.setSelected(true);
		orderButton.setText("Order");
		orderButton
				.setToolTipText("Reorder axes by dragging them across the display.");
		buttonEditGroup.add(orderButton);
		orderButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
		orderButton.setMaximumSize(new java.awt.Dimension(65, 27));
		orderButton.setMinimumSize(new java.awt.Dimension(65, 27));
		orderButton.setPreferredSize(new java.awt.Dimension(65, 27));
		orderButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setEditModeOrder(evt);
			}
		});
		modeBar.add(orderButton);

		brushButton.setFont(new java.awt.Font("Dialog", 0, 10));
		brushButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/org/mediavirus/parvis/gui/brush.gif")));
		brushButton.setText("Brush");
		brushButton.setToolTipText("Brush axes by dragging up or down.");
		buttonEditGroup.add(brushButton);
		brushButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
		brushButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setEditModeBrush(evt);
			}
		});
		modeBar.add(brushButton);

		jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
		modeBar.add(jSeparator2);

		jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
		modeBar.add(jSeparator4);

		displayLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		displayLabel.setText("Display: ");
		modeBar.add(displayLabel);

		axisLabelBox.setFont(new java.awt.Font("Dialog", 0, 10));
		axisLabelBox.setText("axis-labels");
		axisLabelBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showAxisLabelsActionPerformed(evt);
			}
		});
		modeBar.add(axisLabelBox);

		zonesBox.setFont(new java.awt.Font("Dialog", 0, 10));
		zonesBox.setText("zones");
		zonesBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zoneBoxActionPerformed(evt);
			}
		});
		modeBar.add(zonesBox);

		tooltipBox.setFont(new java.awt.Font("Dialog", 0, 10));
		tooltipBox.setSelected(true);
		tooltipBox.setText("tooltips");
		tooltipBox.setMargin(new java.awt.Insets(0, 2, 0, 2));
		tooltipBox.setEnabled(false);
		tooltipBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tooltipBoxActionPerformed(evt);
			}
		});
		modeBar.add(tooltipBox);

		hoverBox.setFont(new java.awt.Font("Dialog", 0, 10));
		hoverBox.setText("line");
		hoverBox.setMargin(new java.awt.Insets(0, 2, 0, 2));
		hoverBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				hoverBoxActionPerformed(evt);
			}
		});
		modeBar.add(hoverBox);

		jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
		jSeparator3.setVisible(true);
		modeBar.add(jSeparator3);

		progressPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.CENTER, 5, 0));

		progressLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		progressLabel.setText("progress:");
		modeBar.add(progressLabel);

		progressBar.setFont(new java.awt.Font("Dialog", 0, 10));
		progressBar.setMaximumSize(new java.awt.Dimension(32767, 18));
		progressBar.setMinimumSize(new java.awt.Dimension(10, 16));
		progressBar.setPreferredSize(new java.awt.Dimension(50, 18));
		progressBar.setStringPainted(true);
		modeBar.add(progressBar);

		timeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		timeLabel.setText("(0.0 s)");
		modeBar.add(timeLabel);

		recCountLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		recCountLabel.setText("# Records Selected: ");
		modeBar.add(recCountLabel);

		countLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		countLabel.setText("0 / 0   ");
		countLabel.setMaximumSize(new java.awt.Dimension(100, 16));
		modeBar.add(countLabel);

		resetBrushButton.setFont(new java.awt.Font("Dialog", 0, 10));
		resetBrushButton.setText("Reset Brush Selection");
		resetBrushButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				resetBrushButtonActionPerformed(evt);
			}
		});
		modeBar.add(resetBrushButton);

		resetAllButton.setFont(new java.awt.Font("Dialog", 0, 10));
		resetAllButton.setText("Reset Axis Selection");
		resetAllButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				resetAllButtonActionPerformed(evt);
			}
		});
		modeBar.add(resetAllButton);

		toolbarPanel.add(modeBar);

		datasourceLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		datasourceLabel.setText("Datasource: ");
		urlBar.add(datasourceLabel);

		urlField.setFont(new java.awt.Font("Dialog", 0, 10));
		urlField.setText("");
		urlField.setMargin(new java.awt.Insets(0, 0, 0, 5));
		urlField.setMinimumSize(null);
		urlField.setPreferredSize(null);
		urlField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				urlFieldActionPerformed(evt);
			}
		});
		urlBar.add(urlField);

		jButton1.setFont(new java.awt.Font("Dialog", 0, 10));
		jButton1.setText("Load File...");
		jButton1.setMargin(new java.awt.Insets(0, 5, 0, 0));
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openItemActionPerformed(evt);
			}
		});
		urlBar.add(jButton1);

		jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
		urlBar.add(jSeparator5);

		progressLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		progressLabel.setText("progress:");
		urlBar.add(progressLabel);

		progressBar.setFont(new java.awt.Font("Dialog", 0, 10));
		progressBar.setMaximumSize(new java.awt.Dimension(32767, 18));
		progressBar.setMinimumSize(new java.awt.Dimension(10, 16));
		progressBar.setPreferredSize(new java.awt.Dimension(70, 18));
		progressBar.setStringPainted(true);
		urlBar.add(progressBar);

		timeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		timeLabel.setText("(0.0 s)");
		urlBar.add(timeLabel);

		toolbarPanel.add(urlBar);

		getContentPane().add(toolbarPanel, java.awt.BorderLayout.NORTH);

		parallelDisplay.setPreferredSize(new java.awt.Dimension(800, 500));
		getContentPane().add(parallelDisplay, java.awt.BorderLayout.CENTER);

		pack();
	}// GEN-END:initComponents

	private void preferencesMenuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_preferencesMenuActionPerformed
		PrefsFrame pf = new PrefsFrame(parallelDisplay);
		pf.show();
	}// GEN-LAST:event_preferencesMenuActionPerformed

	private void saveBrushItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveBrushItemActionPerformed
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return (f.isDirectory() || f.getName().endsWith(".chf"));
			}

			public String getDescription() {
				return "CHF (Channel File) Data Files";
			}
		});
		if (currentPath == null) {
			chooser
					.setCurrentDirectory(new File(System
							.getProperty("user.dir")));
		} else {
			chooser.setCurrentDirectory(currentPath);
		}

		int option = chooser.showSaveDialog(this);

		if (option == JFileChooser.APPROVE_OPTION) {
			if (chooser.getSelectedFile() != null) {
				try {
					parallelDisplay.getCurrentBrush().writeToFile(
							chooser.getSelectedFile(), true);
				} catch (IOException ioex) {
					System.out.println(ioex.getMessage());
				}
			}
		}
	}// GEN-LAST:event_saveBrushItemActionPerformed

	private void showAxisLabelsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_histogramBoxActionPerformed
		parallelDisplay.setBoolPreference("axis-labels", axisLabelBox
				.isSelected());
		parallelDisplay.repaint();
	}// GEN-LAST:event_histogramBoxActionPerformed

	private void histogramBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_histogramBoxActionPerformed
		parallelDisplay.setBoolPreference("histogram", histogramBox
				.isSelected());
		parallelDisplay.repaint();
	}// GEN-LAST:event_histogramBoxActionPerformed

	private void zoneBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_histogramBoxActionPerformed
		parallelDisplay.setBoolPreference("zones", zonesBox.isSelected());
		parallelDisplay.repaint();
	}

	private void fuzzyBrushBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_fuzzyBrushBoxActionPerformed
		if (fuzzyBrushBox.isSelected()) {
			radiusField.setEnabled(true);
			String txt = radiusField.getText();
			if (txt.indexOf('%') > -1) {
				txt = txt.substring(0, txt.indexOf('%'));
			}
			txt = txt.trim();

			int num = Integer.parseInt(txt);
			parallelDisplay.setFloatPreference("brushRadius",
					((float) num) / 100.0f);
		} else {
			radiusField.setEnabled(false);
			parallelDisplay.setFloatPreference("brushRadius", 0.0f);
		}

	}// GEN-LAST:event_fuzzyBrushBoxActionPerformed

	private void resetAllButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_resetAllButtonActionPerformed
		parallelDisplay.resetAll();
	}// GEN-LAST:event_resetAllButtonActionPerformed

	private void resetBrushButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_resetBrushButtonActionPerformed
		parallelDisplay.setCurrentBrush(null);
	}// GEN-LAST:event_resetBrushButtonActionPerformed

	private void hoverBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_hoverBoxActionPerformed
		if (hoverBox.isSelected()) {
			tooltipBox.setEnabled(true);
			parallelDisplay.setBoolPreference("hoverText", tooltipBox
					.isSelected());
			parallelDisplay.setBoolPreference("hoverLine", hoverBox
					.isSelected());
		} else {
			tooltipBox.setEnabled(false);
			parallelDisplay.setBoolPreference("hoverText", false);
			parallelDisplay.setBoolPreference("hoverLine", hoverBox
					.isSelected());
		}
	}// GEN-LAST:event_hoverBoxActionPerformed

	private void radiusFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_radiusFieldActionPerformed
		String txt = radiusField.getText();
		if (txt.indexOf('%') > -1) {
			txt = txt.substring(0, txt.indexOf('%'));
		}
		txt = txt.trim();

		int num = Integer.parseInt(txt);
		parallelDisplay.setFloatPreference("brushRadius",
				((float) num) / 100.0f);
		radiusField.setText(" " + num + " %");
		radiusField.transferFocus();
	}// GEN-LAST:event_radiusFieldActionPerformed

	private void radiusFieldFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_radiusFieldFocusGained
		radiusField.selectAll();
	}// GEN-LAST:event_radiusFieldFocusGained

	private void scaleMinMaxAbsItemActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_scaleMinMaxAbsItemActionPerformed
		parallelDisplay.minMaxAbsScale();
	}// GEN-LAST:event_scaleMinMaxAbsItemActionPerformed

	private void scaleMinMaxItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_scaleMinMaxItemActionPerformed
		parallelDisplay.minMaxScale();
	}// GEN-LAST:event_scaleMinMaxItemActionPerformed

	private void scaleZeroMaxItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_scaleZeroMaxItemActionPerformed
		parallelDisplay.zeroMaxScale();
	}// GEN-LAST:event_scaleZeroMaxItemActionPerformed

	private void setEditModeTranslate(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setEditModeTranslate
		parallelDisplay.setEditMode(ParallelDisplay.TRANSLATE);
		translateButton.setSelected(true);
		translateMenu.setSelected(true);
	}// GEN-LAST:event_setEditModeTranslate

	private void setEditModeScale(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setEditModeScale
		parallelDisplay.setEditMode(ParallelDisplay.SCALE);
		scaleButton.setSelected(true);
		scaleMenu.setSelected(true);
	}// GEN-LAST:event_setEditModeScale

	private void setEditModeOrder(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setEditModeOrder
		parallelDisplay.setEditMode(ParallelDisplay.REORDER);
		orderButton.setSelected(true);
		orderMenu.setSelected(true);
	}// GEN-LAST:event_setEditModeOrder

	private void setEditModeBrush(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setEditModeBrush
		parallelDisplay.setEditMode(ParallelDisplay.BRUSH);
		brushButton.setSelected(true);
		brushMenu.setSelected(true);
	}// GEN-LAST:event_setEditModeBrush

	private void urlFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_urlFieldActionPerformed
		try {
			STFFile f = new STFFile(new URL(urlField.getText()));
			f.addProgressListener(this);

			f.readContents();

			parallelDisplay.setModel(f);
			// setTitle("Parvis - " + f.getName());
			setTitle("Parallel Coordinate Visualization");
		} catch (Exception e) {
			System.out.println(e.toString() + e.getMessage());
		}
	}// GEN-LAST:event_urlFieldActionPerformed

	private void tooltipBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tooltipBoxActionPerformed
		parallelDisplay.setBoolPreference("hoverText", tooltipBox.isSelected());
	}// GEN-LAST:event_tooltipBoxActionPerformed

	File currentPath = null;

	private void openItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openItemActionPerformed
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return (f.isDirectory() || f.getName().endsWith(".stf"));
			}

			public String getDescription() {
				return "STF (Simple Table Format) Data Files";
			}
		});
		if (currentPath == null) {
			chooser
					.setCurrentDirectory(new File(System
							.getProperty("user.dir")));
		} else {
			chooser.setCurrentDirectory(currentPath);
		}

		int option = chooser.showOpenDialog(this);

		if (option == JFileChooser.APPROVE_OPTION) {
			if (chooser.getSelectedFile() != null) {
				currentPath = chooser.getSelectedFile().getParentFile();
				String urltext = "file:///"
						+ chooser.getSelectedFile().getAbsolutePath();
				urltext = urltext.replace('\\', '/');
				System.out.println(">>>> File Path: " + urltext);
				readDataFile(urltext);

			}
		}
	}// GEN-LAST:event_openItemActionPerformed

	/**
	 * TODO: >> Need to replace this methods with covertor -- Matrix - STF format
	 * Note: To read the datafile directly
	 * 
	 * @param urltext
	 */
	
	public void readDataFile(String urltext) {

		try {
			File file = new File(urltext);
			STFFile f = new STFFile(new URL(urltext));
			f.readContents();

			parallelDisplay.setModel(f);
			// setTitle("Parvis - " + f.getName());
			setTitle("Parallel Coordinate View");
		} catch (Exception e) {
			System.out.println(e.toString() + e.getMessage());
		}
	}

	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_exitForm
		//System.exit(0);
		Frame[] frames = PrefsFrame.getFrames();
		
		if (frames != null) {
			for (int i=0; i < frames.length; i++) {
				frames[i].dispose();
			}
		}
		
		frames = CorrelationFrame.getFrames();
		
		if (frames != null) {
			for (int i=0; i < frames.length; i++) {
				frames[i].dispose();
			}
		}
		
		dispose();
	}// GEN-LAST:event_exitForm

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		UIManager.put("org.mediavirus.parvis.gui.ParallelDisplayUI",
				"org.mediavirus.parvis.gui.BasicParallelDisplayUI");
		new MainFrame().show();
	}

	private long progressstart = 0;

	/**
	 * Callback for progress events. Updates the progressbar.
	 */
	public void processProgressEvent(ProgressEvent e) {
		switch (e.getType()) {
		case ProgressEvent.PROGRESS_START:
			progressstart = e.getTimestamp();
			progressBar.setValue(0);
			timeLabel.setText("0 s");

			if (parallelDisplay.getCurrentBrush() == null) {
				// workaround because we are not notified otherways if model
				// changes
				countLabel.setText("0 / " + parallelDisplay.getNumRecords()
						+ "   ");
			}
			break;

		case ProgressEvent.PROGRESS_UPDATE:
			progressBar.setValue((int) (e.getProgress() * 100));
			timeLabel.setText(((e.getTimestamp() - progressstart) / 1000)
					+ " s");
			break;

		case ProgressEvent.PROGRESS_FINISH:
			progressBar.setValue(100);
			timeLabel.setText(((e.getTimestamp() - progressstart) / 1000)
					+ " s");
			break;
		}
		progressLabel.setText(e.getMessage());
		// System.out.println(e.getMessage() + ": " + ((int)(e.getProgress() *
		// 100))+"%");
	}

	public void brushChanged(Brush b) {
	}

	/**
	 * Callback triggered if the brush has been modified. Updates the counter.
	 */
	public void brushModified(Brush b) {
		if (b != null) {
			countLabel.setText(b.getNumBrushed() + " / " + b.getNumValues()
					+ "   ");
		} else {
			countLabel
					.setText("0 / " + parallelDisplay.getNumRecords() + "   ");
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton resetBrushButton;

	private org.mediavirus.parvis.gui.ParallelDisplay parallelDisplay;

	private javax.swing.JMenu fileMenu;

	private javax.swing.JRadioButtonMenuItem brushMenu;

	private javax.swing.JSeparator jSeparator2;

	private javax.swing.JSeparator jSeparator3;

	private javax.swing.JSeparator jSeparator4;

	private javax.swing.JSeparator jSeparator5;

	private javax.swing.JMenuItem preferencesMenu;

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.ButtonGroup menuEditGroup;

	private javax.swing.JButton jButton1;

	private javax.swing.JPanel quickPrefPanel;

	private javax.swing.JMenuItem helpItem;

	private javax.swing.JMenuBar menuBar;

	private javax.swing.JPanel statusPanel;

	private javax.swing.JToolBar modeBar;

	private javax.swing.JMenuItem saveBrushItem;

	private javax.swing.JLabel datasourceLabel;

	private javax.swing.JCheckBox hoverBox;

	private javax.swing.JMenuItem scaleMinMaxAbsItem;

	private javax.swing.JLabel timeLabel;

	private javax.swing.JProgressBar progressBar;

	private javax.swing.JLabel countLabel;

	private javax.swing.JMenuItem scaleZeroMaxItem;

	private javax.swing.JToolBar urlBar;

	private javax.swing.JButton resetAllButton;

	private javax.swing.JMenu viewMenu;

	private javax.swing.JToggleButton translateButton;

	private javax.swing.JToggleButton orderButton;

	private javax.swing.JToggleButton scaleButton;

	private javax.swing.JCheckBox fuzzyBrushBox;

	private javax.swing.JPanel progressPanel;

	private javax.swing.JLabel modeLabel;

	private JLabel recCountLabel;

	private JLabel displayLabel;

	private javax.swing.JMenuItem scaleMinMaxItem;

	private javax.swing.JRadioButtonMenuItem translateMenu;

	private javax.swing.JRadioButtonMenuItem orderMenu;

	private javax.swing.JToggleButton brushButton;

	private javax.swing.JTextField urlField;

	private javax.swing.JPanel toolbarPanel;

	private javax.swing.JRadioButtonMenuItem scaleMenu;

	private javax.swing.JMenu editMenu;

	private javax.swing.JMenuItem openMenu;

	private javax.swing.JTextField radiusField;

	private javax.swing.JCheckBox tooltipBox;

	private javax.swing.JCheckBox histogramBox;

	private javax.swing.JCheckBox zonesBox;

	private javax.swing.JCheckBox axisLabelBox;

	private javax.swing.JLabel progressLabel;

	private javax.swing.ButtonGroup buttonEditGroup;

	private javax.swing.JMenuItem aboutItem;

	private javax.swing.JMenu helpMenu;

	// End of variables declaration//GEN-END:variables

	public org.mediavirus.parvis.gui.ParallelDisplay getParallelDisplay() {
		return parallelDisplay;
	}

	public void internalFrameActivated(InternalFrameEvent arg0) {

	}

	public void internalFrameClosed(InternalFrameEvent arg0) {

	}

	public void internalFrameClosing(InternalFrameEvent arg0) {
		System.out.println("Closing PC view");

	}

	public void internalFrameDeactivated(InternalFrameEvent arg0) {

	}

	public void internalFrameDeiconified(InternalFrameEvent arg0) {

	}

	public void internalFrameIconified(InternalFrameEvent arg0) {

	}

	public void internalFrameOpened(InternalFrameEvent arg0) {

	}

}
