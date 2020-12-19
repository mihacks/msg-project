package com.msg.ttp.encryption.suite;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipOutputStream;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GUI {

	private JFrame frame;
	
	static String dbPath;
	static String inputPath;
	static String outputPath;
	static String newKey;
	
	
	/**
	 * Launch the application.
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
			
			inputPath=args[0];
        	dbPath=args[1];
        	newKey=args[2];
        	outputPath=args[3];
        	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
		JButton btnNewButton = new JButton("DECRYPT");
		btnNewButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				
				final File folder = new File(inputPath);
				try {
					String ispis=Readfile.listFilesForFolder(folder, dbPath);
					textArea.setText(ispis);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		panel.add(btnNewButton);
		
		JButton btnExport = new JButton("EXPORT");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				final File folder = new File(inputPath);
				try {
					output.listFilesForFolder(folder, dbPath, outputPath, newKey);
					
					File fileToZip = new File(outputPath+"/out");
					FileOutputStream fos = new FileOutputStream(outputPath+"/CompressedOut.zip");
				    ZipOutputStream zipOut = new ZipOutputStream(fos);
				    Ziper.zipFile(fileToZip, fileToZip.getName(), zipOut);
			         
			        zipOut.close();
			        fos.close();
			        File brisi=new File(outputPath+"/out");
			        Del.deleteDir(brisi); 
				    JOptionPane.showMessageDialog(null,"Successfully exported!");
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(btnExport);
	}
	
}
