package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TxtTool {
	File file;
	String text = "";
	HashMap<String, String> replaceMap = new HashMap<>();

	public TxtTool() {

	}

	public TxtTool(File file) {
		this.file = file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getText() {
		return this.text;
	}

	public void getInput() {
		String temp = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			while ((temp = reader.readLine()) != null) {
				if (temp.endsWith("--e")) {
					break;
				}
				this.text += temp;
				this.text += "\n";
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void replace() {
		Iterator<Entry<String, String>> iter = replaceMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			this.text = this.text.replaceAll(entry.getKey(), entry.getValue());
		}
	}

	public void add(String header, String tail) {
		this.text = header + this.text + tail;
	}

	protected void setReplace(String toReplace, String replacement) {
		replaceMap.put(toReplace, replacement);
	}

	public void writeFile(String fileName) throws IOException {
		File tempFile = new File(fileName);
		if (!tempFile.exists()) {
			tempFile.createNewFile();
		}
		PrintStream pStream = new PrintStream(new FileOutputStream(tempFile));
		pStream.print(this.text);

	}

	@Override
	public String toString() {
		return this.text;
	}

	public void writeFile() throws IOException {
		writeFile("tempRuslt.txt");
	}

	public void doProcess() {
		setReplace("<", "&lt;");
		setReplace(">", "&gt;");
		replace();
		add("[code lang=\"java\"]", "[/code]");
		add("<pre>", "</pre>");
		try {
			writeFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TxtTool tool = new TxtTool();
		tool.getInput();
		tool.doProcess();
		System.out.println(tool.getText());
	}
}
