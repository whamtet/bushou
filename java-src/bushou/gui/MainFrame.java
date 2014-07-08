package bushou.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JLabel lblAlt;
	private JLabel lblAlt_1;
	private JLabel lblPinyin;
	private JLabel lblHint;
	private JLabel lblMeaning;
	private JLabel lblMain;
	private static final ArrayList<String> lines = new ArrayList<String>();
	private static final boolean YIBAI = false;
	private static String letter = null;

	private String[] split;
	private boolean hintRevealed, pinyinRevealed;
	private int i;

	private final ArrayList<String> myLines = new ArrayList<String>();

	{
		for (String line : lines) {
			myLines.add(line);
		}
				Collections.shuffle(myLines);
	}

	static {
		try {
			String file;
			if (letter != null) {
				file = String.format("csv/%s.csv", letter);
			} else {
				file = YIBAI ? "csv/yibai.csv" : "csv/data.csv";
			}
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file), "UTF8"));
			String line;
			while ((line = br.readLine()) != null) {
				//				System.out.println(line);
				//				boolean b = true;
				//				if (b) break;
				lines.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static final Random random = new Random();
	private static <A> A randomNth(List<A> list) {
		return list.get(random.nextInt(list.size()));
	}

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		String[] abc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
//		for (String s : abc) {
//			String cmd = String.format("touch csv/%s.csv", s);
//			Runtime.getRuntime().exec(cmd);
//		}
		start();
	}
	private static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void setNext(int i) {
		split = myLines.get(i).split(",");
		//		split = lines.get(i).split(",");

		if (YIBAI) {
			lblMain.setText(split[1]);
			lblAlt.setText(split[0]);
			lblPinyin.setText(split[2]);
		} else {
			lblMain.setText(split[0]);
			lblAlt.setText(split[1]);
			lblAlt_1.setText(split[2]);
			lblPinyin.setText(split[3]);
		}

		lblHint.setText("");
		lblMeaning.setText("");
		hintRevealed = false;
		pinyinRevealed = true;
	}
	private void revealHint() {
		if (YIBAI) {
			lblHint.setText(split[3]);
		} else {
			if (split.length > 5) {
				lblHint.setText(split[5]);
			} else {
				lblHint.setText("no hint");
			}
		}
		hintRevealed = true;
	}
	private void revealPinyin() {
		pinyinRevealed = true;
		if (YIBAI) {
			lblPinyin.setText(split[2]);
		} else {
			lblPinyin.setText(split[3]);
		}
	}
	private void revealAnswer() {
		if (YIBAI) {
			lblMeaning.setText(split[4]);
		} else {
			lblMeaning.setText(split[4]);
		}
	}


	/**
	 * Create the frame.
	 */
	public MainFrame() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				switch(e.getKeyChar()) {
				case 'h':
					if (!pinyinRevealed) {
						revealPinyin();
					} else if (!hintRevealed) {
						revealHint();
					} else {
						revealAnswer();
					}
					break;
				case 'n':
					i++;
					if (i == lines.size()) i = 0;
					setNext(i);
					break;
				case 'p':
					i--;
					if (i == -1) i += lines.size();
					setNext(i);
					break;
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		lblMain = new JLabel("Main");
		lblMain.setFont(new Font("Lucida Grande", Font.PLAIN, 32));

		lblAlt = new JLabel("");
		lblAlt.setFont(new Font("Lucida Grande", Font.PLAIN, 32));

		lblAlt_1 = new JLabel("");

		lblPinyin = new JLabel("");

		lblHint = new JLabel("hint");

		lblMeaning = new JLabel("meaning");

		setNext(0);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(138)
										.addComponent(lblAlt)
										.addGap(84)
										.addComponent(lblAlt_1))
										.addGroup(gl_contentPane.createSequentialGroup()
												.addGap(184)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(lblHint)
														.addComponent(lblPinyin)))
														.addGroup(gl_contentPane.createSequentialGroup()
																.addGap(170)
																.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
																		.addComponent(lblMeaning)
																		.addComponent(lblMain))))
																		.addContainerGap(166, Short.MAX_VALUE))
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(39)
						.addComponent(lblMain)
						.addGap(18)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAlt)
								.addComponent(lblAlt_1))
								.addGap(18)
								.addComponent(lblPinyin)
								.addGap(18)
								.addComponent(lblHint)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblMeaning)
								.addContainerGap(61, Short.MAX_VALUE))
				);
		contentPane.setLayout(gl_contentPane);
	}
}
