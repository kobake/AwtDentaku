package jp.clockup.awtdentaku;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main extends Frame
	implements WindowListener, ActionListener{

	public static void main(String[] args) {
		new Main();
	}

	// パーツをメンバ変数として保管しておく
	Label m_label = null;

	public Main(){
		super("電卓");
		this.addWindowListener(this);

		// 上部パネル（テキスト配置部分）
		Panel panelTop = new Panel();
		panelTop.setLayout(new BorderLayout());
		{
			// ラベル追加
			Label label = new Label("0");
			label.setAlignment(Label.RIGHT);
			label.setBackground(Color.LIGHT_GRAY);
			panelTop.add(label, BorderLayout.CENTER);
			Font f = new Font(Font.MONOSPACED, 0, 20);
			label.setFont(f);
			m_label = label;
		}

		// 下部パネル（ボタン配置部分）
		Font f = new Font(Font.MONOSPACED, 0, 20);
		Panel panelKeys = new Panel();
		panelKeys.setLayout(new GridLayout(5, 4)); // 行数、列数
		{
			String[] names = {
					"AC","C","BS","±",
					"7","8", "9", "+",
					"4","5", "6", "-",
					"1","2", "3", "*",
					"0",".", "=", "/",
			};
			for(int i = 0; i < 20; i++){
				Button button = new Button(names[i]);
				button.addActionListener(this);
				button.setFont(f);
				panelKeys.add(button);
			}
		}

		// パネルの貼り付け
		this.add(panelTop, BorderLayout.NORTH);
		this.add(panelKeys, BorderLayout.CENTER);

		// ウィンドウ表示
		setSize(300, 300);
		setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	// ×が押された
	@Override
	public void windowClosing(WindowEvent e) {
		dispose(); // ウィンドウ破棄
	}

	// ウィンドウが閉じた
	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0); // アプリ終了
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		{
			int a = 0;
		}
		{
			int b = 0;
		}
	}

	//String m_text = ""; // ★
	// 3つの状態を保存
	CalcNumber m_left = new CalcNumber();
	CalcNumber m_right = new CalcNumber();
	String m_op = "";
	String m_result = "";

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();   // 押されたボタン
		Button b = (Button)o;       // Button型にキャスト
		String text = b.getLabel(); // Buttonの見た目
		// 特殊ボタンの実装
		if(text.equals("AC")){
			m_left.clear();
			m_right.clear();
			m_op = "";
			m_label.setText(m_left.getForLabel());
		}
		else if(text.equals("C")){
			if(m_op.length() == 0){
				m_left.clear();
			}
			else{
				m_right.clear();
			}
			m_label.setText("");
		}
		else if(text.equals("BS")){
			if(m_op.length() == 0){
				m_left.backspace();
				m_label.setText(m_left.getForLabel());
			}
			else{
				m_right.backspace();
				m_label.setText(m_right.getForLabel());
			}
		}
		// ②演算子が押されたとき
		else if(text.equals("+") || text.equals("-")
				|| text.equals("*") || text.equals("/")){
			m_op = text;
		}
		// ③イコールが押されたとき
		else if(text.equals("=")){ //「=」が押されたら計算を実行する
			float left = 0;
			// 左辺があったら、左辺を採用、
			if(m_left.hasData())left = m_left.toFloat(); //Float.parseFloat(m_left);
			// そうでなかったら、m_result を採用
			else left = Float.parseFloat(m_result);

			float right = m_right.toFloat(); //Float.parseFloat(m_right);
			float result = 0;
			// 計算
			if(m_op.equals("+"))result = left + right;
			else if(m_op.equals("-"))result = left - right;
			else if(m_op.equals("*"))result = left * right;
			else if(m_op.equals("/"))result = left / right;
			m_result = "" + result;
			// 結果表示
			m_label.setText("" + result);
			// 状態クリア
			m_op = "";
			m_left.clear();
			m_right.clear();
		}
		// 小数点が押されたとき
		//else if(text.equals(".")){
		//}
		// ①数字が押されたとき
		else{
			if(m_op.length() == 0){ // 演算子がない
				//m_left = addNumber(m_left, text);
				m_left.addNumber(text);
				m_label.setText(m_left.getForLabel());
				m_result = "";
			}
			else{ // 演算子がある
				//m_right = addNumber(m_right, text);
				m_right.addNumber(text);
				m_label.setText(m_right.getForLabel());
			}
		}
	}

	// 数字を表す。左辺または右辺を保持するために使う。
	class CalcNumber{
		private String mData = "";
		public void clear(){
			mData = "";
		}
		public boolean hasData(){
			return mData.length() > 0;
			/*
			if(mData.length() > 0){
				return true;
			}
			else{
				return false;
			}
			*/
		}
		public void backspace(){
			if(mData.length() > 0){
				mData = mData.substring(0, mData.length() - 1);
			}
		}
		public float toFloat(){
			return Float.parseFloat(mData);
		}
		public String getForLabel(){
			if(mData.length() == 0){
				return "0";
			}
			else{
				return mData;
			}
		}
		// 引数textは「0」～「9」または「.」
		public void addNumber(String text){
			// 入力値がゼロの場合
			if(text.equals("0")){
				if(mData.equals("0")){
				}
				else{
					mData += text;
				}
			}
			// 入力値が小数点の場合
			else if(text.equals(".")){
				// したい
				// 辺の最初で小数点が押された場合は「0.」としたい
				//if(mData.contains(".")){ // 小数点の連続を防止
				if(mData.indexOf(".") != -1){ // 小数点の連続を防止
					// 何もしない
				}
				else{
					if(mData.length() == 0){ // 空っぽだったら「0」を挿入
						mData = "0.";
					}
					else{ // それ以外：普通に小数点追加
						mData += ".";
					}
				}
			}
			// 入力値が1～9の場合
			else{
				if(mData.equals("0")){
					mData = "";
				}
				mData += text;
			}
		}
	}
}

/*
if(text.equals("AC")){
	m_text = "";
}
else if(text.equals("C")){
	m_text = "";
}
else if(text.equals("BS")){
	if(m_text.length() > 0){
		m_text = m_text.substring(0, m_text.length() - 1);
	}
}
*/
