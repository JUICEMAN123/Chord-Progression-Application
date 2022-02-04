import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;

public class ChordProgressionMusicTheoryGame extends JPanel {

	JFrame frame = new JFrame("Music Theory Chord Progressions V2.0");
	
	Player player = new Player();
	JTextField tempoText = new JTextField();
	JTextField chordText = new JTextField();
	JTextField keyText = new JTextField();
	//JTextField keyText = new JTextField();
	JPanel playPanel = new JPanel();
	JCheckBox invBox = new JCheckBox("Extra?");
	JCheckBox instrumBox = new JCheckBox("Random Instruments?");
	JCheckBox arpBox = new JCheckBox("ARP Mode");
	JTextField chordDisplayText = new JTextField();
	JSlider loopSlider = new JSlider(JSlider.HORIZONTAL, 1, 8, 1);
	
	Color backColor = new Color(210, 150, 220);
	Color foreColor = new Color(90, 45, 80);
	Color midColor = new Color(255, 250, 255);
	
	public ChordProgressionMusicTheoryGame() {
		
		setupFrame();
		
		Pattern pattern = new ChordProgression("I IV VI")
				.allChordsAs("$0q $1h $2w")
				.getPattern();
		
		player.play(pattern);

	    //player.play(cp.allChordsAs("$0q $0q $0q $0q $1q $1q $2q $0q"));
		//player.play(cp.allChordsAs("$0 $0 $0 $0 $1 $1 $2 $0").eachChordAs("V0 $0s $1s $2s Rs V1 $!q"));
		
	}
	
	public void defaultText() {
		
		chordText.setText("I-IV-V-I");
		tempoText.setText("130");
		keyText.setText("C");
		
	}
	
	public void play(String mode) throws Exception{
		
		String chords = chordText.getText();
		String[] filtCH = chords.split("[^[iIvV7* ]]+");
		
		String[] chordFilterer = chords.split("[@/^wqseh123456890]");
		System.out.println(Arrays.toString(chordFilterer));
		
		
		chords = "";
		for(String s : chordFilterer) {
			
			chords += s;
			
		}
		
		String temp = " " + chords;
		for(int i = 1; i < loopSlider.getValue(); i++) {
			
			chords += temp;
			
		}
		
		String key = keyText.getText();
		String tempo = tempoText.getText();
		int instrument = (instrumBox.isSelected()) ? (int)(Math.random()*128) : 0;
		//String instrument = (String) instruments.getSelectedItem();

		if(mode.equals("arp")) {
			
			ChordProgression cp = new ChordProgression(chords);
			cp.setKey(key);
			
			String[] choArr = chords.split("[ -]");
			String chordInp = "";
			for(int i = 0; i < choArr.length; i++) {
				
				chordInp += "$" + i + " ";
				
			}
			
			System.out.println(chordInp.length() + Arrays.toString(choArr));
			
			chordDisplayText.setText(cp.toString());
			
			Pattern p = cp.distribute("7%6")
					.allChordsAs(chordInp)
	                .eachChordAs(("$0ia100 $1ia80 $2ia80 $3ia80 $4ia100 $3ia80 $2ia80 $1ia80" + ((!arpBox.isSelected()) ? " $0qa100 R" : "")))
	                .getPattern()
	                .setInstrument(instrument)
	                .setTempo(Integer.parseInt(tempo));
			
			player.play(p);	
			
		}
		
		else if (!invBox.isSelected()){
			
			ChordProgression cp = new ChordProgression(chords);
			cp.setKey(key);
			
			chordDisplayText.setText(cp.toString());
			
			Pattern p = cp.getPattern().setInstrument(instrument).setTempo(Integer.parseInt(tempo));
			
			player.play(p);
			
		}
		
		else if (invBox.isSelected()){
			
			//System.out.println(chords);
			
			System.out.println(Arrays.toString(filtCH));
			for(int i = 0; i < filtCH.length; i++) { 
				
				int ind;
				while((ind = filtCH[i].indexOf(" ")) >= 0) {
					
					filtCH[i] = filtCH[i].substring(0, ind) + filtCH[i].substring(ind+1);
					
				}
				
			}
			
			ChordProgression cp = new ChordProgression(chords);
			System.out.println(chords + " ||| " + cp);
			cp.setKey(key);
			
			int[] chordConverted = new int[cp.getChords().length];
			int[] chordOctaves = new int[cp.getChords().length];
			String[] chordLength = new String[cp.getChords().length];
			
			for(int i = 0; i < chordOctaves.length; i++) {
				
				chordOctaves[i] = 4;
				
			}
			
			for(int i = 0; i < chordLength.length; i++) {
				
				chordLength[i] = "";
				
			}
			System.out.println("CHL22 " + Arrays.toString(filtCH));
			for(int i = 0, invI = 0, octI = 0, lenI = 0; i < filtCH.length; i++) {
				System.out.println("fff: " + Arrays.toString(filtCH) + " " + i);
				System.out.println("CHC " + Arrays.toString(chordConverted));
				System.out.println("CHO " + Arrays.toString(chordOctaves));
				System.out.println("CHL " + Arrays.toString(chordLength));
				if(filtCH[i] == null || filtCH[i].indexOf("-") >= 0) {
					
					invI++;
					octI++;
					lenI++;
					
					if(filtCH[i].indexOf("-") == 0) {
						
						filtCH[i] = filtCH[i].substring(1);
					}
				}
				
				if(filtCH[i].length() == 0) {
					
					continue;
					
				}
				
				char check = filtCH[i].charAt(0);
				filtCH[i] = filtCH[i].substring(1);
				switch(check) {
				
				case '/' :
					
					if(filtCH[i].indexOf("42") >= 0) {
						chordConverted[invI - 1] = 3;
					} else if(filtCH[i].indexOf("64") >= 0 || filtCH[i].indexOf("43") >= 0) {
						chordConverted[invI - 1] = 2;
					} else if(filtCH[i].indexOf("6") >= 0 || filtCH[i].indexOf("65") >= 0) {
						chordConverted[invI - 1] = 1;
					}
					//invI++;
					int temInd;
					if((temInd = filtCH[i].indexOf("-")) >= 0 && temInd + 2 < filtCH[i].length() ) {
						filtCH[i] = filtCH[i].substring(temInd+2);
					} else
						break;
					
				case '@' :
					
					chordLength[lenI] = filtCH[i].substring(0, 1);
					//lenI++;
					if(filtCH[i].length() >= 3) {
						check = filtCH[i].charAt(1);
						filtCH[i] = filtCH[i].substring(2);
					} else
						break;
				
				case '^' :
					
					chordOctaves[octI] = Integer.parseInt(filtCH[i]);
					//octI++;
					break;
					
				}
				
			}
			
			Chord[] chordEdits = cp.getChords();
			
			//System.out.println(Arrays.toString(chordConverted) + "\n" + Arrays.toString(chordEdits));
			
			for(int i = 0; i < chordConverted.length; i++) {
				
				chordEdits[i].setInversion(chordConverted[i]);
				
			}
			
			for(int i = 0; i < chordOctaves.length; i++) {
				
				chordEdits[i].setOctave(chordOctaves[i]);
				
			}
			
			//System.out.println(Arrays.toString(chordOctaves));
			
			String composition = "";
			
			for(int i = 0; i < chordLength.length; i++) {
				
				composition += "$" + i + chordLength[i] + " ";
				
			}
			System.out.println(composition);
			cp = ChordProgression.fromChords(chordEdits);
			
			chordDisplayText.setText(cp.toString());
			
			Pattern p = cp.allChordsAs(composition)
					.getPattern()
					.setInstrument(instrument)
					.setTempo(Integer.parseInt(tempo));
			
			player.play(p);
			
		}
		
		System.out.println();
		
	}
	
	public void setupFrame() {
		BorderLayout b = new BorderLayout();
		setLayout(b);
		frame.setSize(1366, 768);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		setBackground(foreColor);
		
		b.setHgap(15);
		b.setVgap(15);
		
		Container pane = frame.getContentPane();
		
		tempoText.setPreferredSize(new Dimension(400,100));
		tempoText.setText("TEMPO");
		tempoText.setFont(new Font("Serif", Font.BOLD, 40));
		tempoText.setHorizontalAlignment(JTextField.CENTER);
		pane.add(tempoText, BorderLayout.LINE_START);
		
		tempoText.setBackground(backColor);
		tempoText.setForeground(foreColor);
		
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		pane.add(top, BorderLayout.PAGE_START);
		
		chordText.setPreferredSize(new Dimension(1360,150));
		chordText.setText("♪♫♪♫   Chord Progression (with dashes)  ♪♫♪♫");
		chordText.setFont(new Font("Serif", Font.BOLD, 50));
		chordText.setHorizontalAlignment(JTextField.CENTER);
		top.add(chordText, BorderLayout.PAGE_START);
		
		chordText.setBackground(backColor);
		chordText.setForeground(midColor);
		
		keyText.setPreferredSize(new Dimension(400,100));
		keyText.setText("KEY");
		keyText.setFont(new Font("Serif", Font.BOLD, 40));
		keyText.setHorizontalAlignment(JTextField.CENTER);
		pane.add(keyText, BorderLayout.LINE_END);
		
		keyText.setBackground(backColor);
		keyText.setForeground(foreColor);
		
		JButton arppB = new JButton("Arpeggiate");
		arppB.setPreferredSize(new Dimension(200, 100));
		arppB.setFont(new Font("Arial", Font.BOLD, 30));
		arppB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					play("arp");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		JButton playB = new JButton("♪ Play ♫");
		playB.setPreferredSize(new Dimension(200, 100));
		playB.setFont(new Font("Arial", Font.BOLD, 30));
		playB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					play("");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		JButton defB = new JButton("♫ Default");
		defB.setPreferredSize(new Dimension(150, 75));
		defB.setFont(new Font("Arial", Font.BOLD, 22));
		defB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					defaultText();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		invBox.setPreferredSize(new Dimension(160, 100));
		invBox.setHorizontalAlignment(JCheckBox.CENTER);
		invBox.setFont(new Font("Serif", Font.BOLD, 15));
		
		instrumBox.setPreferredSize(new Dimension(160, 100));
		instrumBox.setHorizontalAlignment(JCheckBox.CENTER);
		instrumBox.setFont(new Font("Serif", Font.BOLD, 15));
		
		arpBox.setPreferredSize(new Dimension(160, 100));
		arpBox.setHorizontalAlignment(JCheckBox.CENTER);
		arpBox.setFont(new Font("Serif", Font.BOLD, 15));
		
		JLabel loopLabel = new JLabel("Loop #");
		loopLabel.setPreferredSize(new Dimension(500, 60));
		loopLabel.setFont(new Font("Serif", Font.TRUETYPE_FONT, 20));
		loopLabel.setHorizontalAlignment(JLabel.CENTER);
		
		loopSlider.setPreferredSize(new Dimension(500, 50));
		loopSlider.setPaintTicks(true);
		loopSlider.setMinorTickSpacing(1);
		loopSlider.setMajorTickSpacing(1);
		loopSlider.setPaintLabels(true);
		loopSlider.setName("Loop");
		
		arppB.setBackground(backColor);
		defB.setBackground(backColor);
		playB.setBackground(backColor);
		invBox.setBackground(backColor);
		instrumBox.setBackground(backColor);
		arpBox.setBackground(backColor);
		loopSlider.setBackground(backColor);
		playPanel.setBackground(backColor);
		
		playPanel.add(arppB);
		playPanel.add(playB);
		playPanel.add(invBox);
		playPanel.add(instrumBox);
		playPanel.add(arpBox);
		playPanel.add(defB);
		playPanel.add(loopLabel);
		playPanel.add(loopSlider);
		pane.add(playPanel, BorderLayout.CENTER);
		
		chordDisplayText.setEditable(false);
		chordDisplayText.setText("Chord Display");
		chordDisplayText.setHorizontalAlignment(JTextField.CENTER);
		chordDisplayText.setFont(new Font("Serif", Font.ITALIC, 35));
		chordDisplayText.setPreferredSize(new Dimension(100, 150));
		
		chordDisplayText.setBackground(backColor);
		chordDisplayText.setForeground(midColor);
		
		pane.add(chordDisplayText, BorderLayout.PAGE_END);
		
		JLabel indicators = new JLabel("@l^n{Chord}/i   :   l = length (w, h, q, e, s)     n = octave     i = inversion");
		indicators.setBackground(backColor);
		indicators.setForeground(foreColor);
		indicators.setFont(new Font("Serif", Font.BOLD, 30));
		indicators.setHorizontalAlignment(JLabel.CENTER);
		indicators.setPreferredSize(new Dimension(1360, 45));
		
		top.add(indicators, BorderLayout.PAGE_END);
		
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
		new ChordProgressionMusicTheoryGame();
		
	}

}
