package tool;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.event.ActionEvent;

public class MainWindow {

	public JFrame frmStyx;
	private JTextField toReplace;
	private JTextField replacement;
	private TxtTool tool = new TxtTool();
	private JTextField head;
	private JTextField tail;
	private JTextField nhead;
	private JTextField ntail;
	private JTextField lineHead;
	private JTextField lineTail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmStyx.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		// StructureHelper structureHelper = new StructureHelper();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStyx = new JFrame();
		frmStyx.setTitle("Styx文本工具");
		frmStyx.setBackground(Color.LIGHT_GRAY);
		frmStyx.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmStyx.setBounds(100, 100, 1219, 731);
		frmStyx.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStyx.getContentPane().setLayout(null);

		JLabel label = new JLabel("待处理文本：");
		label.setBounds(14, 13, 110, 18);
		frmStyx.getContentPane().add(label);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 44, 649, 252);
		frmStyx.getContentPane().add(scrollPane);

		JTextPane sText = new JTextPane();
		scrollPane.setViewportView(sText);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 379, 647, 262);
		frmStyx.getContentPane().add(scrollPane_1);

		JTextPane rText = new JTextPane();
		rText.setEditable(false);
		scrollPane_1.setViewportView(rText);

		JLabel label_1 = new JLabel("处理结果：");
		label_1.setBounds(14, 344, 92, 18);
		frmStyx.getContentPane().add(label_1);

		JLabel label_2 = new JLabel("替换：（双击列表项删除）");
		label_2.setBounds(704, 13, 194, 18);
		frmStyx.getContentPane().add(label_2);

		JList<String> list = new JList<String>();
		list.setLocation(704, 51);
		list.setSize(new Dimension(483, 108));
		DefaultListModel<String> model1 = new DefaultListModel<String>();
		list.setModel(model1);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (list.getSelectedIndex() != -1)
					if (e.getClickCount() == 2) {
						model1.remove(list.getSelectedIndex());
						tool.removeReplace(list.getSelectedValue());
						list.setModel(model1);
					}
			}
		});

		frmStyx.getContentPane().add(list);

		JLabel label_3 = new JLabel("添加替换：把");
		label_3.setBounds(704, 172, 92, 18);
		frmStyx.getContentPane().add(label_3);

		toReplace = new JTextField();
		toReplace.setBounds(801, 169, 97, 24);
		frmStyx.getContentPane().add(toReplace);
		toReplace.setColumns(10);

		JLabel label_4 = new JLabel("替换为");
		label_4.setBounds(912, 172, 45, 18);
		frmStyx.getContentPane().add(label_4);

		replacement = new JTextField();
		replacement.setBounds(965, 169, 97, 24);
		frmStyx.getContentPane().add(replacement);
		replacement.setColumns(10);

		JButton button = new JButton("确定");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model1.addElement(toReplace.getText() + "  =>  " + replacement.getText());
				tool.setReplace(toReplace.getText(), replacement.getText());
				list.setModel(model1);
			}
		});
		button.setBounds(1074, 168, 113, 27);
		frmStyx.getContentPane().add(button);

		JButton button_1 = new JButton("执行替换");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tool.setText(sText.getText());
				tool.replace();
				rText.setText(tool.getText());
			}
		});
		button_1.setBounds(1074, 9, 113, 27);
		frmStyx.getContentPane().add(button_1);

		JButton button_2 = new JButton("清空");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sText.setText("");
			}
		});
		button_2.setBounds(14, 298, 82, 27);
		frmStyx.getContentPane().add(button_2);

		JButton button_3 = new JButton("清空");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rText.setText("");
			}
		});
		button_3.setBounds(11, 644, 82, 27);
		frmStyx.getContentPane().add(button_3);

		JButton button_4 = new JButton("从文件打开");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				FileFilter fileFilter = new FileFilter() {

					@Override
					public String getDescription() {
						return "文本文件(*.txt)";
					}

					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}
						String fileName = f.getName();
						int index = fileName.lastIndexOf('.');
						if (index > 0 && index < fileName.length() - 1) {
							String extension = fileName.substring(index + 1).toLowerCase();
							if (extension.equals("txt"))
								return true;
						}
						return false;

					}
				};
				chooser.setFileFilter(fileFilter);
				chooser.showDialog(new JLabel(), "选择文件");
				File file = chooser.getSelectedFile();
				String line = "";
				String result = "";
				try {
					InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
					BufferedReader bReader = new BufferedReader(reader);

					while ((line = bReader.readLine()) != null) {
						result += line;
						result += "\n";
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				sText.setText(result);
			}
		});
		button_4.setBounds(550, 9, 113, 27);
		frmStyx.getContentPane().add(button_4);

		JButton button_5 = new JButton("保存到文件");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir")) {
					public void approveSelection() {
						File savedFile = this.getSelectedFile();

						if (savedFile.exists()) {
							int overwriteSelect = JOptionPane.showConfirmDialog(this,
									"<html><font size=3>文件" + savedFile.getName() + "已存在，是否覆盖?</font><html>", "是否覆盖?",
									JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if (overwriteSelect != JOptionPane.YES_OPTION) {
								return;
							}
						}

						super.approveSelection();
					}
				};
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setSelectedFile(new File("result.txt"));

				FileFilter fileFilter = new FileFilter() {

					@Override
					public String getDescription() {
						return "文本文件(*.txt)";
					}

					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}
						String fileName = f.getName();
						int index = fileName.lastIndexOf('.');
						if (index > 0 && index < fileName.length() - 1) {
							String extension = fileName.substring(index + 1).toLowerCase();
							if (extension.equals("txt"))
								return true;
						}
						return false;

					}
				};
				chooser.setFileFilter(fileFilter);
				chooser.showDialog(new JLabel(), "选择文件");
				File file = chooser.getSelectedFile();
				tool.setText(rText.getText());
				try {
					tool.writeFile(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_5.setBounds(550, 644, 113, 27);
		frmStyx.getContentPane().add(button_5);

		JButton button_6 = new JButton("复制到剪贴板");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable transferable = new StringSelection(rText.getText());
				clipboard.setContents(transferable, null);
				JOptionPane.showConfirmDialog(frmStyx.getContentPane(), new String("复制成功！"), new String("提示"),
						JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			}
		});
		button_6.setBounds(411, 644, 123, 27);
		frmStyx.getContentPane().add(button_6);

		JLabel label_5 = new JLabel("头部增加：");
		label_5.setBounds(704, 248, 92, 18);
		frmStyx.getContentPane().add(label_5);

		head = new JTextField();
		head.setBounds(783, 245, 86, 24);
		frmStyx.getContentPane().add(head);
		head.setColumns(10);

		JLabel label_6 = new JLabel("尾部增加：");
		label_6.setBounds(883, 248, 92, 18);
		frmStyx.getContentPane().add(label_6);

		tail = new JTextField();
		tail.setBounds(958, 245, 86, 24);
		frmStyx.getContentPane().add(tail);
		tail.setColumns(10);

		JButton button_7 = new JButton("执行增加");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool.setText(sText.getText());
				tool.add(head.getText(), tail.getText());
				rText.setText(tool.getText());
			}
		});
		button_7.setBounds(1074, 244, 113, 27);
		frmStyx.getContentPane().add(button_7);

		JLabel label_7 = new JLabel("去掉每行前");
		label_7.setBounds(704, 324, 92, 18);
		frmStyx.getContentPane().add(label_7);

		nhead = new JTextField();
		nhead.setBounds(783, 321, 20, 24);
		frmStyx.getContentPane().add(nhead);
		nhead.setColumns(10);

		JLabel lblNewLabel = new JLabel("个字符");
		lblNewLabel.setBounds(810, 324, 45, 18);
		frmStyx.getContentPane().add(lblNewLabel);

		JButton button_8 = new JButton("执行");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = 0;

				try {
					n = Integer.valueOf(nhead.getText());
				} catch (Exception eNum) {
					JOptionPane.showConfirmDialog(frmStyx.getContentPane(), new String("请输入数字！"), new String("提示"),
							JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
				}
				tool.setText(sText.getText());
				tool.removeLineHeader(n);
				rText.setText(tool.getText());
			}
		});
		button_8.setBounds(867, 320, 63, 27);
		frmStyx.getContentPane().add(button_8);

		JButton button_9 = new JButton("执行");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = 0;

				try {
					n = Integer.valueOf(nhead.getText());
				} catch (Exception eNum) {
					JOptionPane.showConfirmDialog(frmStyx.getContentPane(), new String("请输入数字！"), new String("提示"),
							JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
				}
				tool.setText(sText.getText());
				tool.removeLineTail(n);
				rText.setText(tool.getText());
			}
		});
		button_9.setBounds(1124, 321, 63, 27);
		frmStyx.getContentPane().add(button_9);

		JLabel label_8 = new JLabel("个字符");
		label_8.setBounds(1070, 325, 72, 18);
		frmStyx.getContentPane().add(label_8);

		ntail = new JTextField();
		ntail.setColumns(10);
		ntail.setBounds(1047, 321, 20, 24);
		frmStyx.getContentPane().add(ntail);

		JLabel label_9 = new JLabel("去掉每行末");
		label_9.setBounds(965, 324, 86, 18);
		frmStyx.getContentPane().add(label_9);

		JLabel label_10 = new JLabel("每行前添加：");
		label_10.setBounds(704, 379, 92, 18);
		frmStyx.getContentPane().add(label_10);

		lineHead = new JTextField();
		lineHead.setBounds(793, 379, 72, 24);
		frmStyx.getContentPane().add(lineHead);
		lineHead.setColumns(10);

		JButton btnNewButton = new JButton("执行");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool.setText(sText.getText());
				tool.addLineHead(lineHead.getText());
				rText.setText(tool.getText());
			}
		});
		btnNewButton.setBounds(867, 379, 63, 27);
		frmStyx.getContentPane().add(btnNewButton);

		JLabel label_11 = new JLabel("每行末添加：");
		label_11.setBounds(961, 379, 92, 18);
		frmStyx.getContentPane().add(label_11);

		lineTail = new JTextField();
		lineTail.setColumns(10);
		lineTail.setBounds(1050, 379, 72, 24);
		frmStyx.getContentPane().add(lineTail);

		JButton button_10 = new JButton("执行");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tool.setText(sText.getText());
				tool.addLineTail(lineTail.getText());
				rText.setText(tool.getText());
			}
		});
		button_10.setBounds(1124, 379, 63, 27);
		frmStyx.getContentPane().add(button_10);

	}
}
