package eDLineEditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EDLineEditor {
	public String space=System.getProperty("line.separator");
	private Scanner scanner;
	private String file="";
	static ArrayList<String> buffer=new ArrayList<String>();
    static ArrayList<Information> list = new ArrayList<Information>();
	static ArrayList<Integer> startnumber=new ArrayList<Integer>();
	static ArrayList<Integer> endnumber=new ArrayList<Integer>();
	private int now;
	private int start;
	private int end;
	private int dest;
	private int sid;
	private int revisevision=1;
	private int utime=1;
	boolean trueins=true;
	boolean kmarkchange=false;
	boolean wmark=false;
	String insflag;
	boolean qmark=false;
	boolean has_s_instruction=false;
	int quantity;
	String str1;
	String str2;
	Map<String, KeyInfo> keylist = new HashMap<String, KeyInfo>();
	private String f_str1="";
	private String f_str2="";
	int f_sid=-2;
	/**
	 * 接收用户控制台的输入，解析命令，根据命令参数做出相应处理。
	 * 不需要任何提示输入，不要输出任何额外的内容。
	 * 输出换行时，使用System.out.println()。或者换行符使用System.getProperty("line.separator")。
	 * 
	 * 待测方法为public static void main(String[] args)方法。args不传递参数，所有输入通过命令行进行。
	 * 方便手动运行。
	 * 
	 * 说明：可以添加其他类和方法，但不要删除该文件，改动该方法名和参数，不要改动该文件包名和类名
	 */
	public static void main(String[] args) {
		EDLineEditor aEditor=new EDLineEditor();
		aEditor.getins();
	}
	public void getins() {
		now=buffer.size()-1;
		scanner=new Scanner(System.in);
		String start=scanner.nextLine();
		processstartins(start);
		ArrayList<String> copy=new ArrayList<String>(buffer);
		while(scanner.hasNextLine()) {
			String ins=scanner.nextLine();
			if(trueins==false) {
				ProcesstheIns(ins);
			}
			else {
				if (ins.equals("Q")) {
					copy.clear();
					buffer.clear();
					keylist.clear();
					list.clear();
					break;
				}
				else if (ins.equals("q")) {
					if (qmark) {
						copy.clear();
						buffer.clear();
						keylist.clear();
						list.clear();
						break;
					}
					else {
						if (start.equals("ed")) {
							copy.clear();
							buffer.clear();
							keylist.clear();
							list.clear();
							break;
						}
						else {
							if (wmark) {
								copy.clear();
								buffer.clear();
								keylist.clear();
								list.clear();
								break;
							}
							else {
								if (buffer.equals(copy)) {
									copy.clear();
									buffer.clear();
									keylist.clear();
									list.clear();
									break;
								}
								else {
									System.out.println("?");
									qmark=true;
								}
							}
						}
					}
				}
				else {
					ProcesstheIns(ins);
				}
			}
		}
	}
	public void processstartins(String start){
		String edfiler="ed\\s[0-9A-Za-z_]+";
		Pattern edfilep=Pattern.compile(edfiler);
		Matcher edfilem=edfilep.matcher(start);
		
		if (start.length()==2) {
		}
		else if(start.length()>2&&edfilem.matches()) {
			file=start.substring(3);
			loadfile();
			storestate(now, buffer);
		}
	}
	public void ProcesstheIns(String instruction) {
		if(trueins) {
			String addr="((\\+|\\-)?(\\'[a-z]([\\+\\-]\\d+)*|\\d+|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/\\'&]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'/&]+/))?a";
			Pattern addp=Pattern.compile(addr);
			Matcher addm=addp.matcher(instruction);
			
			String insertr="((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*/\\'&]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'/&]+/)([\\+\\-]\\d+)*)?i";
			Pattern insertp=Pattern.compile(insertr);
			Matcher insertm=insertp.matcher(instruction);
			
			String printlinemarkr="((\\+|\\-)?(\\'[a-z]([\\+\\-]\\d+)*|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'/&]+\\?|/[0-9a-zA-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&/]+/))?=";
			Pattern printlinemarkp=Pattern.compile(printlinemarkr);
			Matcher printlinemarkm=printlinemarkp.matcher(instruction);
			
			String zoomr="((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*/&\\']+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*/\\'&]+/)([\\+\\-]\\d+)?)?z(\\d+)*";
			Pattern zoomp=Pattern.compile(zoomr);
			Matcher zoomm=zoomp.matcher(instruction);
			
			String coversingler="((\\+|\\-)?(\\'[a-z]\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/&\\']+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*/&\\']+/))?c";
			Pattern coversinglep=Pattern.compile(coversingler);
			Matcher coversinglem=coversinglep.matcher(instruction);
			
			String covermanyr="((\\'[a-z]|\\d+|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/&\\']+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/&\\']+/),)?((\\'[a-z]|\\d+|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/&\\']+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/&\\']+/)|,|;)c";
			Pattern covermanyp=Pattern.compile(covermanyr);
			Matcher covermanym=covermanyp.matcher(instruction);
			
			String deletesingler="\\.?((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*/&\\']+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*/&\\']+/))?d";
			Pattern deletesinglep=Pattern.compile(deletesingler);
			Matcher deletesinglem=deletesinglep.matcher(instruction);
			
			String deletemanyr="((\\'[a-z]|[\\+\\-]*\\d+|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'/&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/&\\']+/),)?((\\'[a-z]|\\d+|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/&\\']+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*/\\'&]+/|\\.|\\$)|,|;)d";
			Pattern deletemanyp=Pattern.compile(deletemanyr);
			Matcher deletemanym=deletemanyp.matcher(instruction);
			
			String filenamer="f(\\s[a-zA-Z0-9_\\:\\.\\s,\\+\\-\\$!=?\\*\\'/&]*)*";
			Pattern filenamep=Pattern.compile(filenamer);
			Matcher filenamem=filenamep.matcher(instruction);
			
			String swritesingler="((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'/&]+\\?|/[0-9a-zA-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'/&]+/))w\\s*([0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'/&])*";
			Pattern swritesinglep=Pattern.compile(swritesingler);
			Matcher swritesinglem=swritesinglep.matcher(instruction);
			
			String swritemanyr="((\\d+,)?(\\d+|,|;))?w\\s*([0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'/&])*";
			Pattern swritemanyp=Pattern.compile(swritemanyr);
			Matcher swritemanym=swritemanyp.matcher(instruction);
			
			String bwritesingler="((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*/\\'&]+\\?|/[0-9a-zA-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&/]+/))W\\s*([0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&/])*";
			Pattern bwritesinglep=Pattern.compile(bwritesingler);
			Matcher bwritesinglem=bwritesinglep.matcher(instruction);
			
			String bwritemanyr="((\\d+,)?(\\d+|,|;))?W\\s*([0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*/&\\'])*";
			Pattern bwritemanyp=Pattern.compile(bwritemanyr);
			Matcher bwritemanym=bwritemanyp.matcher(instruction);
			
			String printsingler="\\.?((\\+|\\-)?(\\'[a-z]([\\+\\-]\\d+)*|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*?/\\'&]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*?/\\'&]+/)([\\+\\-]\\d+)?)?p";
			Pattern printsinglep=Pattern.compile(printsingler);
			Matcher printsinglem=printsinglep.matcher(instruction);
			
			String printmanyr="((\\'[a-z]|[\\+\\-]?\\d+|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&/]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&/]+/)([\\+\\-]\\d+)?,)?((\\'[a-z]|[\\+\\-]?\\d+|\\?[a-z0-9A-Z_/\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:/\\.\\s,\\+\\-\\$!=?\\*\\'&]+/|\\.|\\$)([\\+\\-]\\d+)?|,|;)p";
			Pattern printmanyp=Pattern.compile(printmanyr);
			Matcher printmanym=printmanyp.matcher(instruction);
			
			String movesingler="((\\+|\\-)?(\\'[a-z]([\\+\\-]\\d+)*|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&/]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&/]+/))?m((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[a-z0-9A-Z_\\./\\:\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[0-9a-zA-Z_\\./\\:\\s,\\+\\-\\$!=?\\*\\'&]+/))?";
			Pattern movesinglep=Pattern.compile(movesingler);
			Matcher movesinglem=movesinglep.matcher(instruction);
			
			String movemanyr="((\\'[a-z]([\\+\\-]\\d+)*|\\d+|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\']+/),)?((\\'[a-z]|\\d+|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+/|\\.|\\$)|,|;)m((\\+|\\-)?(\\d+|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&]+/))?";
			Pattern movemanyp=Pattern.compile(movemanyr);
			Matcher movemanym=movemanyp.matcher(instruction);
			
			String tiesingler="((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&]+/))?t((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[a-z0-9A-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&]+/)([\\+\\-]\\d+)*)?";
			Pattern tiesinglep=Pattern.compile(tiesingler);
			Matcher tiesinglem=tiesinglep.matcher(instruction);
			
			String tiemanyr="(([\\+\\-]?\\d+|\\'[a-z]|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+/),)?(([\\+\\-]?\\d+|\\'[a-z]|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+/|\\.|\\$)|,|;)t((\\+|\\-)?(\\d+|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&]+/)([\\+\\-]\\d+)*)?";
			Pattern tiemanyp=Pattern.compile(tiemanyr);
			Matcher tiemanym=tiemanyp.matcher(instruction);
			
			String joinr="((([\\+\\-]?\\d+|\\'[a-z]|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+/)([\\+\\-]\\d+)?,)?((\\'[a-z]|\\d+|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+/|\\.|\\$)([\\+\\-]\\d+)?|,|;))?j";
			Pattern joinp=Pattern.compile(joinr);
			Matcher joinm=joinp.matcher(instruction);
			
			String  substitutesingler="((\\+|\\-)?(\\'[a-z]|\\d+|\\.|\\$|\\?[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&]+/))?s(/([0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*&])*/([0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*&])*/([0-9g])*)*";
			Pattern  substitutesinglep=Pattern.compile( substitutesingler);
			Matcher  substitutesinglem= substitutesinglep.matcher(instruction);
			
			String  substitutemanyr="(([\\+\\-]?\\d+|\\'[a-z]|\\.|\\$|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+/)([\\+\\-]\\d+)?,)?((\\d+|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+/|\\.|\\$)([\\+\\-]\\d+)?|,|;)s(/([0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&])*/([0-9a-zA-Z_\\.\\:\\s,\\+\\-\\$!=?\\*\\'&])*/([0-9g])*)*";
			Pattern  substitutemanyp=Pattern.compile( substitutemanyr);
			Matcher  substitutemanym= substitutemanyp.matcher(instruction);
			
			String  keyr="(\\d+|\\.|\\$(\\-\\d+)*|\\?[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+\\?|/[a-z0-9A-Z_\\:\\.\\s,\\+\\-\\$!=?\\*\\'&]+/)*k[a-z]";
			Pattern  keyp=Pattern.compile( keyr);
			Matcher  keym= keyp.matcher(instruction);
			if (addm.matches()) {
				addline(instruction);
				if (start>=buffer.size()+1) {
					System.out.println("?");
					trueins=true;
				} else {
					trueins=false;
					insflag="a";
					wmark=false;
				}
			}
			else if (insertm.matches()) {
				insertline(instruction);
				if (start<0) {
					start=0;
					trueins=false;
					insflag="i";
					wmark=false;
				}
				else if (start>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					trueins=false;
					insflag="i";
					wmark=false;
				}
			}
			else if (printsinglem.matches()) {
				printsingleline(instruction);
				if (kmarkchange==false) {
					if (start<0||start>=buffer.size()) {
						System.out.println("?");
					} else {
						System.out.println(buffer.get(start));
					}
					now=start;
				}
				else {
					kmarkchange=false;
				}
				trueins=true;
			}
			else if (printmanym.matches()) {
				printmanyline(instruction);
				if (start<0||end<start|end>=buffer.size()) {
					System.out.println("?");
				} else {
					for (int i = start; i <=end; i++) {
						System.out.println(buffer.get(i));
						now=i;
					}
				}
				trueins=true;
			}
			else if (printlinemarkm.matches()) {
				printlinemark(instruction);
			}
			else if (zoomm.matches()) {
				zoomline(instruction);
				insflag="z";
				trueins=true;
			}
			else if (keym.matches()) {
				keyline(instruction);
				insflag="k";
				trueins=true;
			}
			else if (coversinglem.matches()) {
				coversingleline(instruction);
				if (start<0||start>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					end=start;
					trueins=false;
					insflag="c";
					wmark=false;
				}
				now=start;
			}
			else if (covermanym.matches()) {
				covermanyline(instruction);
				if (start<0||end<start||end>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					trueins=false;
					insflag="c";
					wmark=false;
				}
			}
			else if (deletesinglem.matches()) {
				deletesingleline(instruction);
				if (start<0||start>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					end=start;
					delete();
					trueins=true;
					wmark=false;
				}
			}
			else if (deletemanym.matches()) {
				deletemanyline(instruction);
				if (start<0||end<start||end>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					delete();
					trueins=true;
					wmark=false;
				}
			}
			else if (joinm.matches()) {
				joinline(instruction);
				if (start<0||end<start||end>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					join();
					trueins=true;
					wmark=false;
				}
			}
			else if (movesinglem.matches()) {
				movesingleline(instruction);
				if (start<0||start>=buffer.size()||dest<0||dest>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				}
				else {
					end=start;
					move();
					trueins=true;
					wmark=false;
				}
			}
			else if (movemanym.matches()) {
				movemanyline(instruction);
				if (start<0||end<start||end>buffer.size()||dest<0||dest>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					move();
					trueins=true;
					wmark=false;
				}
			}
			else if (tiesinglem.matches()) {
				tiesingleline(instruction);
				if (start<0||start>=buffer.size()||dest<0||dest>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					end=start;
					tie();
					trueins=true;
					wmark=false;
				}
			}
			else if (tiemanym.matches()) {
				tiemanyline(instruction);
				if (start<0||start>=buffer.size()||end<start||dest<0||dest>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				} else {
					tie();
					trueins=true;
					wmark=false;
				}
			}
			else if (substitutesinglem.matches()) {
				substitutesingleline(instruction);
				
				if (start<0||start>=buffer.size()||dest<0||dest>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				}
				else {
					end=start;
					substitute();
					trueins=true;
					wmark=false;
				}
			}
			else if (substitutemanym.matches()) {
				substitutemanyline(instruction);
				if (start<0||end>=buffer.size()||end<start||dest<0||dest>=buffer.size()) {
					System.out.println("?");
					trueins=true;
				}
				else {
					trueins=true;
					wmark=false;
					substitute();
				}
			}
			else if (filenamem.matches()) {
				filename(instruction);
				trueins=true;
			}
			else if (swritesinglem.matches()) {
				String[] m=instruction.split(" ");
				if (m.length==1) {
					if (file.equals("")) {
						System.out.println("?");
					}
				}
				else {
					file=m[1];
				}
				boolean isappend=false;
				boolean ismany=false;
				writesingleline(instruction,isappend);
				writein(isappend,ismany);
				trueins=true;
			}
			else if (swritemanym.matches()) {
				String[] m=instruction.split(" ");
				if (m.length==1) {
					if (file.equals("")) {
						System.out.println("?");
					}
				}
				else {
					file=m[1];
				}
				boolean isappend=false;
				boolean ismany=true;
				writemanyline(instruction,isappend);
				writein(isappend,ismany);
				trueins=true;
			}
			else if (bwritesinglem.matches()) {
				String[] m=instruction.split(" ");
				if (m.length==1) {
					if (file.equals("")) {
						System.out.println("?");
					}
				}
				else {
					file=m[1];
				}
				boolean isappend=true;
				boolean ismany=false;
				writesingleline(instruction,isappend);
				writein(isappend,ismany);
				trueins=true;
			}
			else if (bwritemanym.matches()) {
				String[] m=instruction.split(" ");
				if (m.length==1) {
					if (file.equals("")) {
						System.out.println("?");
					}
				}
				else {
					file=m[1];
				}
				boolean isappend=true;
				boolean ismany=true;
				writemanyline(instruction,isappend);
				writein(isappend,ismany);
				trueins=true;
			}
			else if (instruction.equals("u")) {
					undo();
					trueins=true;
			}
			else {
				System.out.println("?");
				trueins=true;
			}
		}
		else {
			if(insflag.equals("a")) {
				if (instruction.equals(".")) {
					trueins=true;
					start=start-1;
					now=start;
					changekeynow();
					storestate(now,buffer);
				}
				else {
					add(instruction);
				}
			}
			else if(insflag.equals("i")) {
				if (instruction.equals(".")) {
					start=start-1;
					now=start;
					changekeynow();
					trueins=true;
					storestate(now,buffer);
				}
				else {
					insert(instruction);
				}
			}
			else if (insflag.equals("c")) {	
				if (instruction.equals(".")) {
					if (start<=end) {
						for (int i = start;i<=end; i++) {
							buffer.remove(start);
						}
					}
					start=start-1;
					now=start;
					trueins=true;
					changekeynow();
					storestate(now,buffer);
				}
				else {
					if (start<=end) {
						buffer.set(start, instruction);
						start=start+1;
					}
					else {
						buffer.add(start, instruction);
						start=start+1;
					}
				}
			}
		}
	}
	public void loadfile() {
		try {
			File my=new File(file);
			if (!my.exists()) {
				try {
					my.createNewFile();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			FileReader fr=new FileReader(my);
			BufferedReader br=new BufferedReader(fr);
			String line = null;
	        while ((line=br.readLine()) != null) {  
	        	buffer.add(line);
	        }
	        now=buffer.size()-1;
	        br.close();
	        fr.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void addline(String ins) {
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,ins.length()-1))+1;
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,ins.length()-1))+1;
		}
		else if (ins.substring(0, 1).equals(".")) {
			start=now+1;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last+1;
		}
		else if (ins.substring(0, 1).equals("'")) {
			start=keylist.get(ins.substring(1, 2)).getNow()+1;
			if (ins.substring(2, 3).equals("-")) {
				start=start-Integer.parseInt(ins.substring(3, ins.length()-1));
			}
			else if (ins.substring(2, 3).equals("+")) {
				start=start+Integer.parseInt(ins.substring(3, ins.length()-1));
			}
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			start=dealwithdvd(str)+1;
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			start=dealwithques(str)+1;
		}
		else if (ins.substring(0,1).equals("a")) {
			start=now+1;
		}
		else {
			start=(Integer.parseInt(ins.substring(0,ins.length()-1))-1)+1;
		}
	}
	public void add(String ins) {
		buffer.add(start, ins);
		start=start+1;
	}
	public void insertline(String ins) {
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,ins.length()-1));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,ins.length()-1));
		}
		else if (ins.substring(0,1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
		}
		else if (ins.substring(0,1).equals("i")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("'")) {
			start=keylist.get(ins.substring(1, 2)).getNow();
			if (ins.substring(2, 3).equals("-")) {
				start=start-Integer.parseInt(ins.substring(3, ins.length()-1));
			}
			else if (ins.substring(2, 3).equals("+")) {
				start=start+Integer.parseInt(ins.substring(3, ins.length()-1));
			}
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			start=dealwithdvd(str);
			if (ins.substring(dvd2+1, dvd2+2).equals("-")) {
				start=start-Integer.parseInt(ins.substring(dvd2+2, ins.length()-1));
			}
			else if (ins.substring(dvd2+1, dvd2+2).equals("+")) {
				start=start+Integer.parseInt(ins.substring(dvd2+2, ins.length()-1));
			}
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			start=dealwithques(str);
			if (ins.substring(ques2+1, ques2+2).equals("-")) {
				start=start-Integer.parseInt(ins.substring(ques2+2, ins.length()-1));
			}
			else if (ins.substring(ques2+1, ques2+2).equals("+")) {
				start=start+Integer.parseInt(ins.substring(ques2+2, ins.length()-1));
			}
		}
		else {
			start=(Integer.parseInt(ins.substring(0,ins.length()-1))-1);
		}
	}
	public void insert(String ins) {
		buffer.add(start, ins);
		start=start+1;
	}
	public void printlinemark(String ins) {
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,ins.length()-1));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,ins.length()-1));
		}
		else if (ins.substring(0,1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
		}
		else if (ins.substring(0, 1).equals("'")) {
			if (!keylist.containsKey(ins.subSequence(1, 2))) {
				System.out.println("?");
				kmarkchange=true;
			}else {
				start=keylist.get(ins.substring(1, 2)).getNow();
				if (ins.substring(2, 3).equals("-")) {
					start=start-Integer.parseInt(ins.substring(3, ins.length()-1));
				}
				else if (ins.substring(2, 3).equals("+")) {
					start=start+Integer.parseInt(ins.substring(3, ins.length()-1));
				}
			}
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			start=dealwithdvd(str);
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			start=dealwithques(str);
		}
		else if (ins.substring(0,1).equals("=")) {
			start=buffer.size()-1;
		}
		else {
			start=Integer.parseInt(ins.substring(0,ins.length()-1))-1;
		}
		if (kmarkchange==false) {
			System.out.println(start+1);
		}
		else {
			kmarkchange=false;
		}
	}
	public void zoomline(String ins) {
		int z=ins.indexOf("z");
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,z));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now-Integer.parseInt(ins.substring(1,z));
		}
		else if (ins.substring(0,1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			start=dealwithdvd(str);
			if (z==dvd2+1) {
				
			}
			else if (ins.substring(dvd2+1, dvd2+2).equals("-")) {
				start=start-Integer.parseInt(ins.substring(dvd2+2, z));
			}
			else if (ins.substring(dvd2+1, dvd2+2).equals("+")) {
				System.out.println("here");
				start=start+Integer.parseInt(ins.substring(dvd2+2, z));
			}
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			start=dealwithques(str);
			if (z==ques2+1) {
				
			}
			else if (ins.substring(ques2+1, ques2+2).equals("-")) {
				start=start-Integer.parseInt(ins.substring(ques2+2, z));
			}
			else if (ins.substring(ques2+1, ques2+2).equals("+")) {
				start=start+Integer.parseInt(ins.substring(ques2+2, z));
			}
		}
		else if (ins.substring(0,1).equals("z")) {
			start=now+1;
		}
		else {
			start=Integer.parseInt(ins.substring(0,z))-1;
		}
		if (z==ins.length()-1) {
			end=buffer.size()-1;
		}
		else {
			int n=Integer.parseInt(ins.substring(z+1));
			if((n+start)>=buffer.size()) {
				end=buffer.size()-1;
			}
			else {
				end=n+start;
			}
		}
		if (start>end) {//???
			System.out.println(buffer.get(buffer.size()-1));
		}
		else {
			for (int i = start; i <=end; i++) {
				System.out.println(buffer.get(i));
				now=i;
			}
		}
	}
	public void coversingleline(String ins) {
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,ins.length()-1));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,ins.length()-1));
		}
		else if (ins.substring(0,1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
		}
		else if (ins.substring(0,1).equals("c")) {
			start=now;
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			start=dealwithdvd(str);
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			start=dealwithques(str);
		}
		else {
			start=Integer.parseInt(ins.substring(0,ins.length()-1))-1;
		}
		end=start;
	}
	public void covermanyline(String ins) {
		int c=ins.indexOf("c");
		if(ins.substring(0, 1).equals(",")) {
			start=0;
			end=buffer.size()-1;
		}
		else if(ins.substring(0, 1).equals(";")) {
			start=now;
			end=buffer.size()-1;
		}
		else {
			int com=ins.indexOf(",");
			if (ins.substring(0, 1).equals("/")) {
				int divid2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1, divid2);
				start=dealwithdvd(str);
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1, ques2);
				start=dealwithques(str);
			}
			else {
				start=Integer.parseInt(ins.substring(0,com))-1;
			}
			if (ins.substring(com+1, com+2).equals("/")) {
				int divid2=ins.substring(com+2).indexOf("/")+com+2;
				String str=ins.substring(com+2,divid2);
				end=dealwithdvd(str);
			}
			else if (ins.substring(com+1, com+2).equals("?")) {
				int ques2=ins.substring(com+2).indexOf("?")+com+2;
				String str=ins.substring(com+2,ques2);
				end=dealwithques(str);
			}
			else {
				end=Integer.parseInt(ins.substring(com+1,c))-1;
			}
		}
	}
	public int dealwithdvd(String str) {
		int result = -1;
		for (int i =now+1; i < buffer.size(); i++) {
			if(buffer.get(i).contains(str)) {
				result=i;
				break;
			}
		}
		if (result==-1) {
			for (int i = 0; i < now+1; i++) {
				if (buffer.get(i).contains(str)) {
					result=i;
					break;
				}
			}
		}
		return result;
	}
	public int dealwithques(String str) {
		int result=-1;
		for (int i = now-1; i > -1; i--) {
			if (buffer.get(i).contains(str)) {
				result=i;
				break;
			}
		}
		if (result==-1) {
			for (int i = buffer.size()-1; i >now-1; i--) {
				if (buffer.get(i).contains(str)) {
					result=i;
					break;
				}
			}
		}
		return result;
	}
	public void filename(String ins) {
		if (ins.equals("f")) {
			if (file.equals("")) {
				System.out.println("?");
			}
			else {
				System.out.println(file);
			}
		}
		else {
			file=ins.substring(2);
		}
	}
	public void writesingleline(String ins,boolean isappend) {
		int w;
		if (isappend) {
			w=ins.indexOf("W");
		}
		else {
			w=ins.indexOf("w");
		}
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,w));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,w));
		}
		else if (ins.substring(0, 1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
		}
		else if (ins.substring(0, 1).equals("'")) {
			start=keylist.get(ins.substring(1, 2)).getNow();
			if (ins.substring(2, 3).equals("-")) {
				start=start-Integer.parseInt(ins.substring(3, w));
			}
			else if (ins.substring(2, 3).equals("+")) {
				start=start+Integer.parseInt(ins.substring(3, w));
			}
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			start=dealwithdvd(str);
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			start=dealwithques(str);
		}
		else if (ins.substring(0,1).equals("w")) {
			start=now;
		}
		else if (ins.substring(0,1).equals("W")) {
			start=now;
		}
		else {
			start=(Integer.parseInt(ins.substring(0,w))-1);
		}
			end=start;
	}
	public void writemanyline(String ins,boolean isappend) {
		int w;
		if (isappend) {
			w=ins.indexOf("W");
			if(ins.substring(0, 1).equals(",")) {
				start=0;
				end=buffer.size()-1;
			}
			else if(ins.substring(0, 1).equals(";")) {
				start=now;
				end=buffer.size()-1;
			}
			else if(ins.substring(0, 1).equals("W")) {
				start=0;
				end=buffer.size()-1;
			}
			else {
				int com=ins.indexOf(",");
				start=Integer.parseInt(ins.substring(0,com))-1;
				end=Integer.parseInt(ins.substring(com+1,w))-1;
			}
		}
		else {
			w=ins.indexOf("w");
			if(ins.substring(0, 1).equals(",")) {
				start=0;
				end=buffer.size()-1;
			}
			else if(ins.substring(0, 1).equals(";")) {
				start=now;
				end=buffer.size()-1;
			}
			else if(ins.substring(0, 1).equals("w")) {
				start=0;
				end=buffer.size()-1;
			}
			else {
				int com=ins.indexOf(",");
				start=Integer.parseInt(ins.substring(0,com))-1;
				end=Integer.parseInt(ins.substring(com+1,w))-1;
			}
		}
	}
	public void writein(boolean isappend,boolean ismany) {
		if (isappend) {
			if (ismany&&(!(start==end))) {
				try {
					File my=new File(file);
					if (!my.exists()) {
						try {
							my.createNewFile();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					FileWriter fw=new FileWriter(my,true);
					BufferedWriter bw=new BufferedWriter(fw);
					if (my.length()==0) {
						for (int i = start; i < end; i++) {
							bw.append(buffer.get(i));
							bw.append(space);
						}
						bw.append(buffer.get(end));
					}
					else {
//						bw.append(space);
						for (int i = start; i < end; i++) {
							bw.append(buffer.get(i));
							bw.append(space);
						}
						bw.append(buffer.get(end));
					}
			        bw.close();
			        fw.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				wmark=true;
			}
			else {
				try {
					File my=new File(file);
					if (!my.exists()) {
						try {
							my.createNewFile();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					FileWriter fw=new FileWriter(my,true);
					BufferedWriter bw=new BufferedWriter(fw);
					if (my.exists()) {
//						bw.append(space);
						bw.append(buffer.get(start));
						bw.append(space);
					}
			        bw.close();
			        fw.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				wmark=true;
			}
		}
		else {
			if (ismany&&(!(start==end))) {
				try {
					File my=new File(file);
					if (!my.exists()) {
						try {
							my.createNewFile();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					FileWriter fw=new FileWriter(my);
					BufferedWriter bw=new BufferedWriter(fw);
					for (int i = start; i < end; i++) {
						bw.write(buffer.get(i));
						bw.write(space);
					}
					bw.write(buffer.get(end));
			        bw.close();
			        fw.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				wmark=true;
			}
			else {
				try {
					File my=new File(file);
					if (!my.exists()) {
						try {
							my.createNewFile();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					FileWriter fw=new FileWriter(my);
					BufferedWriter bw=new BufferedWriter(fw);
						bw.write(buffer.get(start));
			        bw.close();
			        fw.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				wmark=true;
			}
		}
	}
	public void printsingleline(String ins) {
		if (ins.equals("p")) {
			start=now;
		}
		else {
			if(ins.substring(0, 2).equals(".-")) {
				start=now-Integer.parseInt(ins.substring(2,ins.length()-1));
			}
			else if (ins.substring(0, 1).equals("-")) {
				start=now-Integer.parseInt(ins.substring(1,ins.length()-1));
			}
			else if(ins.substring(0, 2).equals(".+")) {
				start=now+Integer.parseInt(ins.substring(2,ins.length()-1));
			}
			else if (ins.substring(0, 1).equals("+")) {
				start=now+Integer.parseInt(ins.substring(1,ins.length()-1));
			}
			else if (ins.substring(0,1).equals(".")) {
				start=now;
			}
			else if (ins.substring(0, 1).equals("'")) {
				if (!keylist.containsKey(ins.subSequence(1, 2))) {
					System.out.println("?");
					kmarkchange=true;
				}
				else {
					start=keylist.get(ins.substring(1, 2)).getNow();
					if (ins.substring(2, 3).equals("-")) {
						start=start-Integer.parseInt(ins.substring(3, ins.length()-1));
					}
					else if (ins.substring(2, 3).equals("+")) {
						start=start+Integer.parseInt(ins.substring(3, ins.length()-1));
					}
				}
			}
			else if (ins.substring(0, 1).equals("$")) {
				int last=buffer.size()-1;
				start=last;
				if (ins.substring(1, 2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(2, ins.length()-1));
				}
			}
			else if (ins.substring(0,1).equals("/")) {
				int dvd2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1,dvd2);
				start=dealwithdvd(str);
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1,ques2);
				start=dealwithques(str);
			}
			else {
				start=Integer.parseInt(ins.substring(0,ins.length()-1))-1;
			}
		}
	}
	public void printmanyline(String ins) {
		int p=ins.indexOf("p");
		if(ins.substring(0, 1).equals(",")) {
			start=0;
			end=buffer.size()-1;
		}
		else if(ins.substring(0, 1).equals(";")) {
			start=now;
			end=buffer.size()-1;
		}
		else {
			int com=ins.indexOf(",");
			if (ins.substring(0, 1).equals(".")) {
				if (com==1) {
					start=now;
				}
				else if (ins.substring(1, 2).equals("-")) {
					start=now-Integer.parseInt(ins.substring(2,com));
				}
				else if (ins.substring(1, 2).equals("+")) {
					start=now+Integer.parseInt(ins.substring(2,com));
				}
			}
			else if (ins.substring(0, 1).equals("$")) {
				if (com==1) {
					start=buffer.size()-1;
				}
				else if (ins.substring(1, 2).equals("-")) {
					start=buffer.size()-1-Integer.parseInt(ins.substring(2,com));
				}
				else if (ins.substring(1, 2).equals("+")) {
					start=buffer.size()-1+Integer.parseInt(ins.substring(2,com));
				}
			}
			else if (ins.substring(0, 1).equals("'")) {
				start=keylist.get(ins.substring(1, 2)).getNow();
				if (ins.substring(2, 3).equals("-")) {
					start=start-Integer.parseInt(ins.substring(3, com));
				}
				else if (ins.substring(2, 3).equals("+")) {
					start=start+Integer.parseInt(ins.substring(3, com));
				}
			}
			else if (ins.substring(0, 1).equals("/")) {
				int divid2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1, divid2);
				start=dealwithdvd(str);
				if (com==divid2+1) {
					start=start;
				}
				else if (ins.substring(divid2+1,divid2+2).equals("-")) {
					start=start-Integer.parseInt(ins.substring(divid2+2,com));
				}
				else if (ins.substring(divid2+1,divid2+2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(divid2+2,com));
				}
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1, ques2);
				start=dealwithques(str);
				if (com==ques2+1) {
					start=start;
				}
				else if (ins.substring(ques2+1,ques2+2).equals("-")) {
					start=start-Integer.parseInt(ins.substring(ques2+2,com));
				}
				else if (ins.substring(ques2+1,ques2+2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(ques2+2,com));
				}
			}
			else {
				start=Integer.parseInt(ins.substring(0,com))-1;
			}
			//end
			if (ins.substring(com+1, com+2).equals(".")) {
				if (p==com+2) {
					end=now;
				}
				else if (ins.substring(com+2, com+3).equals("-")) {
					end=now-Integer.parseInt(ins.substring(com+3,p));
				}
				else if (ins.substring(com+2, com+3).equals("+")) {
					end=now+Integer.parseInt(ins.substring(com+3,p));
				}
			}
			else if (ins.substring(com+1, com+2).equals("$")) {
				end=buffer.size()-1;
				if (p==com+2) {
					end=end;
				}
				else if (ins.substring(com+2, com+3).equals("-")) {
					end=end-Integer.parseInt(ins.substring(com+3,p));
				}
				else if (ins.substring(com+2, com+3).equals("+")) {
					end=end+Integer.parseInt(ins.substring(com+3,p));
				}
			}
			else if (ins.substring(com+1, com+2).equals("'")) {
				end=keylist.get(ins.substring(com+2, com+3)).getNow();
				if (ins.substring(com+3, com+4).equals("-")) {
					end=end-Integer.parseInt(ins.substring(com+4,p));
				}
				else if (ins.substring(2, 3).equals("+")) {
					end=end+Integer.parseInt(ins.substring(com+4,p));
				}
			}
			else if (ins.substring(com+1, com+2).equals("/")) {
				int divid2=ins.substring(com+2).indexOf("/")+com+2;
				String str=ins.substring(com+2,divid2);
				end=dealwithdvd(str);
				if (p==divid2+1) {
					end=end;
				}
				else if (ins.substring(divid2+1, divid2+2).equals("-")) {
					end=end-Integer.parseInt(ins.substring(divid2+2,p));
				}
				else if (ins.substring(divid2+1, divid2+2).equals("+")) {
					end=end+Integer.parseInt(ins.substring(divid2+2,p));
				}
			}
			else if (ins.substring(com+1, com+2).equals("?")) {
				int ques2=ins.substring(com+2).indexOf("?")+com+2;
				String str=ins.substring(com+2,ques2);
				end=dealwithques(str);
				if (p==ques2+1) {
					end=end;
				}
				else if (ins.substring(ques2+1, ques2+2).equals("-")) {
					end=end-Integer.parseInt(ins.substring(ques2+2,p));
				}
				else if (ins.substring(ques2+1, ques2+2).equals("+")) {
					end=end+Integer.parseInt(ins.substring(ques2+2,p));
				}
			}
			else {
				end=Integer.parseInt(ins.substring(com+1,p))-1;
			}
		}
	}
	public void deletesingleline(String ins) {
		if (ins.equals("d")) {
			start=now;
		}
		else {
			if(ins.substring(0, 2).equals(".-")) {
				start=now-Integer.parseInt(ins.substring(2,ins.length()-1));
			}
			else if (ins.substring(0, 1).equals("-")) {
				start=now-Integer.parseInt(ins.substring(1,ins.length()-1));
			}
			else if(ins.substring(0, 2).equals(".+")) {
				start=now+Integer.parseInt(ins.substring(2,ins.length()-1));
			}
			else if (ins.substring(0, 1).equals("+")) {
				start=now+Integer.parseInt(ins.substring(1,ins.length()-1));
			}
			else if (ins.substring(0,1).equals(".")) {
				start=now;
			}
			else if (ins.substring(0, 1).equals("$")) {
				int last=buffer.size()-1;
				start=last;
			}
			else if (ins.substring(0, 1).equals("'")) {
				start=keylist.get(ins.substring(1, 2)).getNow();
				if (ins.substring(2, 3).equals("-")) {
					start=start-Integer.parseInt(ins.substring(3, ins.length()-1));
				}
				else if (ins.substring(2, 3).equals("+")) {
					start=start+Integer.parseInt(ins.substring(3, ins.length()-1));
				}
			}
			else if (ins.substring(0,1).equals("/")) {
				int dvd2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1,dvd2);
				start=dealwithdvd(str);
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1,ques2);
				start=dealwithques(str);
			}
			else {
				start=Integer.parseInt(ins.substring(0,ins.length()-1))-1;
			}
		}
	}
	public void deletemanyline(String ins) {
		int d=ins.indexOf("d");
		if(ins.substring(0, 1).equals(",")) {
			start=0;
			end=buffer.size()-1;
		}
		else if(ins.substring(0, 1).equals(";")) {
			start=now;
			end=buffer.size()-1;
		}
		else {
			int com=ins.indexOf(",");
			if (ins.substring(0, 1).equals(".")) {
				start=now;
			}
			else if (ins.substring(0, 1).equals("$")) {
				start=buffer.size()-1;
			}
			else if (ins.substring(0, 1).equals("+")) {
				start=now+Integer.parseInt(ins.substring(1, com));
			}
			else if (ins.substring(0, 1).equals("-")) {
				start=now-Integer.parseInt(ins.substring(1, com));
			}
			else if (ins.substring(0, 1).equals("/")) {
				int divid2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1, divid2);
				start=dealwithdvd(str);
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1, ques2);
				start=dealwithques(str);
			}
			else {
				start=Integer.parseInt(ins.substring(0,com))-1;
			}
			if (ins.substring(com+1, com+2).equals(".")) {
				end=now;
			}
			else if (ins.substring(com+1, com+2).equals("$")) {
				end=buffer.size()-1;
			}
			else if (ins.substring(com+1, com+2).equals("/")) {
				int divid2=ins.substring(com+2).indexOf("/")+com+2;
				String str=ins.substring(com+2,divid2);
				end=dealwithdvd(str);
			}
			else if (ins.substring(com+1, com+2).equals("?")) {
				int ques2=ins.substring(com+2).indexOf("?")+com+2;
				String str=ins.substring(com+2,ques2);
				end=dealwithques(str);
			}
			else {
				end=Integer.parseInt(ins.substring(com+1,d))-1;
			}
		}
	}
	public void delete(){
		for (int i = start; i <= end; i++) {
			buffer.remove(start);
		}
		if ((buffer.size()-1)>=start) {
			now=start;
		}
		else {
			now=start-1;
		}
		changekeynow();
		storestate(now, buffer);
	}
	public void movesingleline(String ins) {
		int m=ins.indexOf("m");
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,m));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,m));
		}
		else if (ins.substring(0,1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
		}
		else if (ins.substring(0,1).equals("m")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("'")) {
			start=keylist.get(ins.substring(1, 2)).getNow();
			if (ins.substring(2, 3).equals("-")) {
				start=start-Integer.parseInt(ins.substring(3, m));
			}
			else if (ins.substring(2, 3).equals("+")) {
				start=start+Integer.parseInt(ins.substring(3, m));
			}
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			if (str.contains("m")) {
				m=dvd2+ins.substring(dvd2+1).indexOf("m")+1;
			}
			start=dealwithdvd(str);
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			if (str.contains("m")) {
				m=ques2+ins.substring(ques2+1).indexOf("m")+1;
			}
			start=dealwithques(str);
		}
		else {
			start=Integer.parseInt(ins.substring(0,m))-1;
		}
		if (m==ins.length()-1) {
			dest=now;
		}
		else {
			if(ins.substring(m+1, m+2).equals("-")) {
				dest=now-Integer.parseInt(ins.substring(m+2));
			}
			else if(ins.substring(m+1, m+2).equals("+")) {
				dest=now+Integer.parseInt(ins.substring(m+2));
			}
			else if (ins.substring(m+1, m+2).equals(".")) {
				dest=now;
			}
			else if (ins.substring(m+1, m+2).equals("$")) {
				int last=buffer.size()-1;
				dest=last;
			}
			else if (ins.substring(m+1,m+2).equals("/")) {
				int dvd2=ins.substring(m+2).indexOf("/")+m+2;
				String str=ins.substring(m+2,dvd2);
				dest=dealwithdvd(str);
			}
			else if (ins.substring(m+1,m+2).equals("?")) {
				int ques2=ins.substring(m+2).indexOf("?")+m+2;
				String str=ins.substring(m+2,ques2);
				dest=dealwithques(str);
			}
			else {
				dest=(Integer.parseInt(ins.substring(m+1))-1);
			}
		}
	}
	public void movemanyline(String ins) {
		int m=ins.indexOf("m");
		if(ins.substring(0, 1).equals(",")) {
			start=0;
			end=buffer.size()-1;
		}
		else if(ins.substring(0, 1).equals(";")) {
			start=now;
			end=buffer.size()-1;
		}
		else {
			int com=ins.indexOf(",");
			if (ins.substring(0, 1).equals(".")) {
				start=now;
			}
			else if (ins.substring(0, 1).equals("$")) {
				start=buffer.size()-1;
			}
			else if (ins.substring(0, 1).equals("'")) {
				start=keylist.get(ins.substring(1, 2)).getNow();
				if (ins.substring(2, 3).equals("-")) {
					start=start-Integer.parseInt(ins.substring(3, com));
				}
				else if (ins.substring(2, 3).equals("+")) {
					start=start+Integer.parseInt(ins.substring(3, com));
				}
			}
			else if (ins.substring(0, 1).equals("/")) {
				int divid2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1, divid2);
				start=dealwithdvd(str);
				if (str.contains(",")) {
					com=divid2+ins.substring(divid2+1).indexOf(",")+1;
				}
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1, ques2);
				start=dealwithques(str);
				if (str.contains(",")) {
					com=ques2+ins.substring(ques2+1).indexOf(",")+1;
				}
			}
			else {
				start=Integer.parseInt(ins.substring(0,com))-1;
			}
			if (ins.substring(com+1, com+2).equals(".")) {
				end=now;
			}
			else if (ins.substring(com+1, com+2).equals("$")) {
				end=buffer.size()-1;
			}
			else if (ins.substring(com+1, com+2).equals("/")) {
				int divid2=ins.substring(com+2).indexOf("/")+com+2;
				String str=ins.substring(com+2,divid2);
				end=dealwithdvd(str);
			}
			else if (ins.substring(com+1, com+2).equals("?")) {
				int ques2=ins.substring(com+2).indexOf("?")+com+2;
				String str=ins.substring(com+2,ques2);
				end=dealwithques(str);
			}
			else {
				end=Integer.parseInt(ins.substring(com+1,m))-1;
			}
		}
		if (m==ins.length()-1) {
			dest=now;
		}
		else {
			if(ins.substring(m+1, m+2).equals("-")) {
				dest=now-Integer.parseInt(ins.substring(m+2));
			}
			else if(ins.substring(m+1, m+2).equals("+")) {
				dest=now+Integer.parseInt(ins.substring(m+2));
			}
			else if (ins.substring(m+1, m+2).equals(".")) {
				dest=now;
			}
			else if (ins.substring(m+1, m+2).equals("$")) {
				int last=buffer.size()-1;
				dest=last;
			}
			else if (ins.substring(m+1,m+2).equals("/")) {
				int dvd2=ins.substring(m+2).indexOf("/")+1+m+1;
				String str=ins.substring(m+2,dvd2);
				dest=dealwithdvd(str);
			}
			else if (ins.substring(m+1,m+2).equals("?")) {
				int ques2=ins.substring(m+2).indexOf("?")+1+m+1;
				String str=ins.substring(m+2,ques2);
				dest=dealwithques(str);
			}
			else {
				dest=(Integer.parseInt(ins.substring(m+1))-1);
			}
		}
	}
	public void tiesingleline(String ins) {//注意tie是在dest之后插入
		int t=ins.indexOf("t");
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,t));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,t));
		}
		else if (ins.substring(0,1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
		}
		else if (ins.substring(0,1).equals("t")) {
			start=now;
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			start=dealwithdvd(str);
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			start=dealwithques(str);
		}
		else {
			start=Integer.parseInt(ins.substring(0,t))-1;
		}
		if (t==ins.length()-1) {
			dest=now;
		}
		else {
			if(ins.substring(t+1, t+2).equals("-")) {
				dest=now-Integer.parseInt(ins.substring(t+2));
			}
			else if(ins.substring(t+1, t+2).equals("+")) {
				dest=now+Integer.parseInt(ins.substring(t+2));
			}
			else if (ins.substring(t+1, t+2).equals(".")) {
				dest=now;
			}
			else if (ins.substring(t+1, t+2).equals("$")) {
				int last=buffer.size()-1;
				dest=last;
			}
			else if (ins.substring(t+1,t+2).equals("/")) {
				int dvd2=ins.substring(t+2).indexOf("/")+t+2;
				String str=ins.substring(t+2,dvd2);
				dest=dealwithdvd(str);
			}
			else if (ins.substring(t+1,t+2).equals("?")) {
				int ques2=ins.substring(t+2).indexOf("?")+t+2;
				String str=ins.substring(t+2,ques2);
				dest=dealwithques(str);
				if (ques2==ins.length()-1) {
					
				}
				else if (ins.substring(ques2+1, ques2+2).equals("-")) {
					dest=dest-Integer.parseInt(ins.substring(ques2+2));
				}
				else if (ins.substring(ques2+1, ques2+2).equals("+")) {
					dest=dest+Integer.parseInt(ins.substring(ques2+2));
				}
			}
			else {
				dest=(Integer.parseInt(ins.substring(t+1))-1);
			}
		}
	}
	public void tiemanyline(String ins) {
		int t=ins.indexOf("t");
		if(ins.substring(0, 1).equals(",")) {
			start=0;
			end=buffer.size()-1;
		}
		else if(ins.substring(0, 1).equals(";")) {
			start=now;
			end=buffer.size()-1;
		}
		else {
			int com=ins.indexOf(",");
			if (ins.substring(0, 1).equals("/")) {
				int divid2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1, divid2);
				start=dealwithdvd(str);
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1, ques2);
				start=dealwithques(str);
			}
			else if (ins.substring(0, 1).equals("-")) {
				start=now-Integer.parseInt(ins.substring(1, com));
			}
			else if (ins.substring(0, 1).equals("+")) {
				start=now+Integer.parseInt(ins.substring(1, com));
			}
			else {
				start=Integer.parseInt(ins.substring(0,com))-1;
			}
			
			if (ins.substring(com+1, com+2).equals("/")) {
				int divid2=ins.substring(com+2).indexOf("/")+com+2;
				String str=ins.substring(com+2,divid2);
				if (str.contains("t")) {
					t=divid2+ins.substring(divid2+1).indexOf("t")+1;
				}
				end=dealwithdvd(str);
			}
			else if (ins.substring(com+1, com+2).equals("?")) {
				int ques2=ins.substring(com+2).indexOf("?")+com+2;
				String str=ins.substring(com+2,ques2);
				if (str.contains("t")) {
					t=ques2+ins.substring(ques2+1).indexOf("t")+1;
				}
				end=dealwithques(str);
			}
			else if (ins.substring(com+1, com+2).equals("-")) {
				end=now-Integer.parseInt(ins.substring(com+2,t));
			}
			else if (ins.substring(com+1, com+2).equals("+")) {
				end=now+Integer.parseInt(ins.substring(com+2,t));
			}
			else {
				end=Integer.parseInt(ins.substring(com+1,t))-1;
			}
		}
		if (t==ins.length()-1) {
			dest=now;
		}
		else {
			if(ins.substring(t+1, t+2).equals("-")) {
				dest=now-Integer.parseInt(ins.substring(t+2));
			}
			else if(ins.substring(t+1, t+2).equals("+")) {
				dest=now+Integer.parseInt(ins.substring(t+2));
			}
			else if (ins.substring(t+1, t+2).equals(".")) {
				dest=now;
			}
			else if (ins.substring(t+1, t+2).equals("$")) {
				int last=buffer.size()-1;
				dest=last;
				if (t+2==ins.length()) {
					
				}
				else if (ins.substring(t+2, t+3).equals("-")) {
					dest=dest-Integer.parseInt(ins.substring(t+3));
				}
			}
			else if (ins.substring(t+1,t+2).equals("/")) {
				int dvd2=ins.substring(t+2).indexOf("/");
				String str=ins.substring(t+2,dvd2);
				dest=dealwithdvd(str);
			}
			else if (ins.substring(t+1,t+2).equals("?")) {
				int ques2=ins.substring(t+2).indexOf("?");
				String str=ins.substring(t+2,ques2);
				dest=dealwithques(str);
			}
			else {
				dest=(Integer.parseInt(ins.substring(t+1))-1);
			}
		}
	}
	public void move() {
		if (start==dest) {
			int length=end-start+1;
			now=dest-1+length;
		}
		else if (start<dest) {
			ArrayList<String> temp=new ArrayList<String>();
			for (int i = start; i <=end; i++) {
				temp.add(buffer.get(i));
			}
			for (int i = start; i <=end; i++) {
				buffer.remove(start);
			}
			int fakestart=dest-(end-start);
			int length=end-start+1;
			for (int i =length-1; i>=0 ; i--) {
				buffer.add(fakestart, temp.get(i));
			}
			now=dest-length+length;
		}
		else if (start>dest) {
			ArrayList<String> temp=new ArrayList<String>();
			for (int i = start; i <=end; i++) {
				temp.add(buffer.get(i));
			}
			for (int j = start; j <=end; j++) {
				buffer.remove(start);
			}
			int length=end-start+1;
			for (int i = length-1; i>=0; i--) {
				buffer.add(dest+1, temp.get(i));
			}
			now=dest+length;
		}
		changekeynow();
		storestate(now,buffer);
	}
	public void tie() {
		ArrayList<String> temp=new ArrayList<String>();
		for (int i = start; i <=end; i++) {
			temp.add(buffer.get(i));
		}
		int length=end-start+1;
		for (int i =length-1; i>=0 ; i--) {
			buffer.add(dest+1, temp.get(i));
		}
		now=dest+length;
		storestate(now, buffer);
	}
	public void joinline(String ins) {
		int j=ins.indexOf("j");
		if(ins.substring(0, 1).equals(",")) {
			start=0;
			end=buffer.size()-1;
		}
		else if(ins.substring(0, 1).equals(";")) {
			start=now;
			end=buffer.size()-1;
		}
		else if (ins.subSequence(0, 1).equals("j")) {
			start=now;
			end=now+1;
		}
		else {
			int com=ins.indexOf(",");
			if (ins.substring(0, 1).equals("/")) {
				int divid2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1, divid2);
				if (str.contains(",")) {
					com=ins.substring(divid2).indexOf(",")+divid2;
				}
				start=dealwithdvd(str);
				if (divid2+1==com) {
					
				}
				else if (ins.subSequence(divid2+1, divid2+2).equals("-")) {
					start=start-Integer.parseInt(ins.substring(divid2+2, com));
				}
				else if (ins.subSequence(divid2+1, divid2+2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(divid2+2, com));
				}
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1, ques2);
				start=dealwithques(str);
				if (ques2+1==com) {
					
				}
				else if (ins.subSequence(ques2+1, ques2+2).equals("-")) {
					start=start-Integer.parseInt(ins.substring(ques2+2, com));
				}
				else if (ins.subSequence(ques2+1, ques2+2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(ques2+2, com));
				}
			}
			else if (ins.substring(0, 1).equals("-")) {
				start=now-Integer.parseInt(ins.substring(1, com));
			}
			else if (ins.substring(0, 1).equals("+")) {
				start=now+Integer.parseInt(ins.substring(1, com));
			}
			else if (ins.substring(0, 1).equals("'")) {
				start=keylist.get(ins.substring(1, 2)).getNow();
				if (ins.substring(2, 3).equals("-")) {
					start=start-Integer.parseInt(ins.substring(3, com));
				}
				else if (ins.substring(2, 3).equals("+")) {
					start=start+Integer.parseInt(ins.substring(3, com));
				}
			}
			else if (ins.substring(0, 1).equals(".")) {
				start=now;
				if (1==com) {
					
				}
				else if (ins.subSequence(1, 2).equals("-")) {
					start=start-Integer.parseInt(ins.substring(2, com));
				}
				else if (ins.subSequence(1, 2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(2, com));
				}
			}
			else if (ins.substring(0, 1).equals("$")) {
				start=buffer.size()-1;
			}
			else {
				start=Integer.parseInt(ins.substring(0,com))-1;
			}
			if (ins.substring(com+1, com+2).equals("/")) {
				int divid2=ins.substring(com+2).indexOf("/")+com+2;
				String str=ins.substring(com+2,divid2);
				end=dealwithdvd(str);
				if (divid2+1==j) {
					
				}
				else if (ins.subSequence(divid2+1, divid2+2).equals("-")) {
					end=end-Integer.parseInt(ins.substring(divid2+2, j));
				}
				else if (ins.subSequence(divid2+1, divid2+2).equals("+")) {
					end=end+Integer.parseInt(ins.substring(divid2+2, j));
				}
			}
			else if (ins.substring(com+1, com+2).equals("?")) {
				int ques2=ins.substring(com+2).indexOf("?")+com+2;
				String str=ins.substring(com+2,ques2);
				end=dealwithques(str);
				if (ques2+1==j) {
					
				}
				else if (ins.subSequence(ques2+1, ques2+2).equals("-")) {
					end=end-Integer.parseInt(ins.substring(ques2+2, j));
				}
				else if (ins.subSequence(ques2+1, ques2+2).equals("+")) {
					end=end+Integer.parseInt(ins.substring(ques2+2, j));
				}
			}
			else if (ins.substring(com+1, com+2).equals("-")) {
				end=now-Integer.parseInt(ins.substring(com+1,j));
			}
			else if (ins.substring(com+1, com+2).equals("+")) {
				end=now+Integer.parseInt(ins.substring(com+1,j));
			}
			else if (ins.substring(com+1, com+2).equals(".")) {
				end=now;
				if (com+2==j) {
					
				}
				else if (ins.subSequence(com+2, com+3).equals("-")) {
					end=end-Integer.parseInt(ins.substring(com+3, j));
				}
				else if (ins.subSequence(com+2, com+3).equals("+")) {
					end=end+Integer.parseInt(ins.substring(com+3, j));
				}
			}
			else if (ins.substring(com+1, com+2).equals("$")) {
				end=buffer.size()-1;
				if (com+2==j) {
					end=end;
				}
				else if (ins.substring(com+2, com+3).equals("-")) {
					end=end-Integer.parseInt(ins.substring(com+3, j));
				}
				else if (ins.substring(com+2, com+3).equals("+")) {
					end=end+Integer.parseInt(ins.substring(com+3, j));
				}
			}
			else if (ins.substring(com+1, com+2).equals("'")) {
				end=keylist.get(ins.substring(com+2, com+3)).getNow();
				if (ins.substring(com+3, com+4).equals("-")) {
					end=end-Integer.parseInt(ins.substring(com+4, ins.length()-1));
				}
				else if (ins.substring(2, 3).equals("+")) {
					end=end+Integer.parseInt(ins.substring(com+4, ins.length()-1));
				}
			}
			else {
				end=Integer.parseInt(ins.substring(com+1,j))-1;
			}
		}
	}
	public void join() {
		for (int i = end; i>start; i--) {
			String latter=buffer.get(i);
			String former=buffer.get(i-1);
			buffer.remove(i);
			buffer.set(i-1, former+latter);
		}
		now=start;
		changekeynow();
		storestate(now, buffer);
	}
	public void substitutesingleline(String ins) {
		int s=ins.indexOf("s");
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,s));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,s));
		}
		else if (ins.substring(0,1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("'")) {
			start=keylist.get(ins.substring(1, 2)).getNow();
			if (ins.substring(2, 3).equals("-")) {
				start=start-Integer.parseInt(ins.substring(3, s));
			}
			else if (ins.substring(2, 3).equals("+")) {
				start=start+Integer.parseInt(ins.substring(3, s));
			}
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
		}
		else if (ins.substring(0,1).equals("s")) {
			start=now;
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			if (str.contains("s")) {
				s=s+ins.substring(s+1).indexOf("s")+1;
			}
			start=dealwithdvd(str);
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			if (str.contains("s")) {
				s=s+ins.substring(s+1).indexOf("s")+1;
			}
			start=dealwithques(str);
		}
		else {
			start=Integer.parseInt(ins.substring(0,s))-1;
		}
		if (s==ins.length()-1) {
			str1=f_str1;
			str2=f_str2;
			sid=f_sid;
		}
		else {
			int firstdivid=s+1;
			int seconddivid=ins.substring(firstdivid+1).indexOf("/")+firstdivid+1;
			int thirddivid=ins.substring(seconddivid+1).indexOf("/")+seconddivid+1;
			if ((firstdivid+1)==seconddivid) {
				str1="";
			}
			else {
				str1=ins.substring(firstdivid+1, seconddivid);
			}
			if ((seconddivid+1)==thirddivid) {
				str2="";
			}
			else {
				str2=ins.substring(seconddivid+1, thirddivid);
			}
			if (thirddivid==(ins.length()-1)) {
				sid=0;
			}
			else {
				if (ins.substring((thirddivid+1)).equals("g")) {
					sid=-1;
				}
				else {
					sid=Integer.parseInt(ins.substring(thirddivid+1))-1;
				}
			}
			f_sid=sid;
			f_str1=str1;
			f_str2=str2;
		}
	}
	public void substitutemanyline(String ins) {
		int s=ins.indexOf("s");
		if(ins.substring(0, 1).equals(",")) {
			start=0;
			end=buffer.size()-1;
		}
		else if(ins.substring(0, 1).equals(";")) {
			start=now;
			end=buffer.size()-1;
		}
		else if (ins.subSequence(0, 1).equals("j")) {
			start=now;
			end=now+1;
		}
		else {
			int com=ins.indexOf(",");
			if (ins.substring(0, 1).equals("/")) {
				int divid2=ins.substring(1).indexOf("/")+1;
				String str=ins.substring(1, divid2);
				if (str.contains(",")) {
					com=ins.substring(divid2).indexOf(",")+divid2;
				}
				if (str.contains("s")) {
					s=ins.substring(divid2).indexOf("s")+divid2;
				}
				start=dealwithdvd(str);
				if (divid2+1==com) {
					
				}
				else if (ins.subSequence(divid2+1, divid2+2).equals("-")) {
					start=start-Integer.parseInt(ins.substring(divid2+2, com));
				}
				else if (ins.subSequence(divid2+1, divid2+2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(divid2+2, com));
				}
			}
			else if (ins.substring(0,1).equals("?")) {
				int ques2=ins.substring(1).indexOf("?")+1;
				String str=ins.substring(1, ques2);
				if (str.contains(",")) {
					com=ins.substring(ques2).indexOf(",")+ques2;
				}
				if (str.contains("s")) {
					s=ins.substring(ques2).indexOf("s")+ques2;
				}				
				start=dealwithques(str);
				if (ques2+1==com) {
					
				}
				else if (ins.subSequence(ques2+1, ques2+2).equals("-")) {
					start=start-Integer.parseInt(ins.substring(ques2+2, com));
				}
				else if (ins.subSequence(ques2+1, ques2+2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(ques2+2, com));
				}
			}
			else if (ins.substring(0, 1).equals("-")) {
				start=now-Integer.parseInt(ins.substring(1, com));
			}
			else if (ins.substring(0, 1).equals("+")) {
				start=now+Integer.parseInt(ins.substring(1, com));
			}
			else if (ins.substring(0, 1).equals(".")) {
				start=now;
				if (1==com) {
					
				}
				else if (ins.subSequence(1, 2).equals("-")) {
					start=start-Integer.parseInt(ins.substring(2, com));
				}
				else if (ins.subSequence(1, 2).equals("+")) {
					start=start+Integer.parseInt(ins.substring(2, com));
				}
			}
			else if (ins.substring(0, 1).equals("$")) {
				start=buffer.size()-1;
			}
			else {
				start=Integer.parseInt(ins.substring(0,com))-1;
			}
			if (ins.substring(com+1, com+2).equals("/")) {
				int divid2=ins.substring(com+2).indexOf("/")+com+2;
				String str=ins.substring(com+2,divid2);
				end=dealwithdvd(str);
				if (divid2+1==s) {
					
				}
				else if (ins.subSequence(divid2+1, divid2+2).equals("-")) {
					end=end-Integer.parseInt(ins.substring(divid2+2, s));
				}
				else if (ins.subSequence(divid2+1, divid2+2).equals("+")) {
					end=end+Integer.parseInt(ins.substring(divid2+2, s));
				}
			}
			else if (ins.substring(com+1, com+2).equals("?")) {
				int ques2=ins.substring(com+2).indexOf("?")+com+2;
				String str=ins.substring(com+2,ques2);
				end=dealwithques(str);
				if (ques2+1==s) {
					
				}
				else if (ins.subSequence(ques2+1, ques2+2).equals("-")) {
					end=end-Integer.parseInt(ins.substring(ques2+2, s));
				}
				else if (ins.subSequence(ques2+1, ques2+2).equals("+")) {
					end=end+Integer.parseInt(ins.substring(ques2+2, s));
				}
			}
			else if (ins.substring(com+1, com+2).equals("-")) {
				end=now-Integer.parseInt(ins.substring(com+1,s));
			}
			else if (ins.substring(com+1, com+2).equals("+")) {
				end=now+Integer.parseInt(ins.substring(com+1,s));
			}
			else if (ins.subSequence(com+1, com+2).equals(".")) {
				end=now;
				if (com+2==s) {
					
				}
				else if (ins.subSequence(com+2, com+3).equals("-")) {
					end=end-Integer.parseInt(ins.substring(com+3, s));
				}
				else if (ins.subSequence(com+2, com+3).equals("+")) {
					end=end+Integer.parseInt(ins.substring(com+3, s));
				}
			}
			else if (ins.subSequence(com+1, com+2).equals("$")) {
				end=buffer.size()-1;
			}
			else {
				end=Integer.parseInt(ins.substring(com+1,s))-1;
			}
		}
		if (s==ins.length()-1) {
			str1=f_str1;
			str2=f_str2;
			sid=f_sid;
		}
		else {
			int firstdivid=s+1;
			int seconddivid=ins.substring(firstdivid+1).indexOf("/")+firstdivid+1;
			int thirddivid=ins.substring(seconddivid+1).indexOf("/")+seconddivid+1;
			if ((firstdivid+1)==seconddivid) {
				str1="";
			}
			else {
				str1=ins.substring(firstdivid+1, seconddivid);
			}
			if ((seconddivid+1)==thirddivid) {
				str2="";
			}
			else {
				str2=ins.substring(seconddivid+1, thirddivid);
			}
			if (thirddivid==(ins.length()-1)) {
				sid=0;
			}
			else {
				if (ins.substring((thirddivid+1)).equals("g")) {
					sid=-1;
				}
				else {
					sid=Integer.parseInt(ins.substring(thirddivid+1))-1;
				}
			}
			f_sid=sid;
			f_str1=str1;
			f_str2=str2;
		}
	}
	public void substitute() {
		boolean sfindmark=false;
		boolean sthislineoutofbound=true;
		for (int i = start; i <=end; i++) {
			if (!(sid==-2)) {
				findstr(buffer.get(i), str1);
				if (!(quantity==-1)) {
					sfindmark=true;
					if (sid==-1) {
						sthislineoutofbound=false;
						String result="";
						for (int j = 0; j < quantity; j++) {
							int kaishi;
							int jieshu=startnumber.get(j);
							if (j==0) {
								kaishi=0;
							}
							else {
								kaishi=endnumber.get(j-1)+1;
							}
							result=result+buffer.get(i).substring(kaishi, jieshu)+str2;
						}
						result=result+buffer.get(i).substring(endnumber.get(quantity-1)+1);
						buffer.set(i, result);
						now=i;
					}
					else {
						if (sid<quantity) {
							sthislineoutofbound=false;
							int kaishi=startnumber.get(sid);
							int jieshu=endnumber.get(sid);
							String temp=buffer.get(i).substring(0, kaishi)+str2+buffer.get(i).substring((jieshu+1));
							buffer.set(i, temp);
							now=i;
						}
					}
				}
				startnumber.clear();
				endnumber.clear();
			}
		}
		changekeynow();
		if (sfindmark==false||sthislineoutofbound==true||sid==-2) {
			System.out.println("?");
		}
		else {
			storestate(now, buffer);
		}
	}
	public void findstr(String line,String str1) {
		String ins=line;
		int length=str1.length();
		if (ins.indexOf(str1)==-1) {
			quantity=-1;
		}
		else {
			quantity=0;
			int back=0;
	        while(ins.indexOf(str1)!=-1) {
	            int i = ins.indexOf(str1);//可以直接用
	            int temp=i+back;
	            startnumber.add(temp);
	            endnumber.add(temp+length-1);
	            quantity++;
	            ins = ins.substring(i+1);
	            back=temp+1;
	        }
		}
	}
	public void keyline(String ins) {
		int k=ins.indexOf("k");
		if(ins.substring(0, 1).equals("-")) {
			start=now-Integer.parseInt(ins.substring(1,ins.length()-2));
		}
		else if(ins.substring(0, 1).equals("+")) {
			start=now+Integer.parseInt(ins.substring(1,ins.length()-2));
		}
		else if (ins.substring(0,1).equals(".")) {
			start=now;
		}
		else if (ins.substring(0, 1).equals("$")) {
			int last=buffer.size()-1;
			start=last;
			if (ins.substring(1, 2).equals("-")) {
				start=start-Integer.parseInt(ins.substring(2, k));
			}
		}
		else if (ins.substring(0,1).equals("k")) {
			start=now;
		}
		else if (ins.substring(0,1).equals("/")) {
			int dvd2=ins.substring(1).indexOf("/")+1;
			String str=ins.substring(1,dvd2);
			start=dealwithdvd(str);
		}
		else if (ins.substring(0,1).equals("?")) {
			int ques2=ins.substring(1).indexOf("?")+1;
			String str=ins.substring(1,ques2);
			start=dealwithques(str);
		}
		else {
			start=Integer.parseInt(ins.substring(0,k))-1;
		}
		String keychar=ins.substring(ins.length()-1);
		KeyInfo KeyInfo=new KeyInfo(start, buffer.get(start));
		keylist.put(keychar, KeyInfo);//未对删除之后操作
	}
	public void undo() {
		now=list.get(list.size()-(utime+1)).getNow();
		int oldsize=list.get(list.size()-(utime+1)).getBuffer().size();
		buffer.clear();
		for (int i = 0; i < oldsize; i++) {
			String addin=list.get(list.size()-(utime+1)).getBuffer().get(i);
			buffer.add(addin);
		}
		utime=utime+1;
	}
	public void storestate(int now,ArrayList<String> buffer) {
		if (revisevision==1) {
			ArrayList<String> buffer1=new ArrayList<String>();
			buffer1.addAll(buffer);
			Information state1=new Information(now,buffer1);
			list.add(state1);
		}
		else if (revisevision==2) {
			ArrayList<String> buffer2=new ArrayList<String>();
			buffer2.addAll(buffer);
			Information state2=new Information(now,buffer2);
			list.add(state2);
		}
		else if (revisevision==3) {
			ArrayList<String> buffer3=new ArrayList<String>();
			buffer3.addAll(buffer);
			Information state3=new Information(now,buffer3);
			list.add(state3);
		}
		else if (revisevision==4) {
			ArrayList<String> buffer4=new ArrayList<String>();
			buffer4.addAll(buffer);
			Information state4=new Information(now,buffer4);
			list.add(state4);
		}
		revisevision=revisevision+1;
	}
	public void changekeynow() {
		String key="";
		for (String string : keylist.keySet()) {
			String content=keylist.get(string).getContent();
			if (!buffer.contains(content)) {
				key=string;
				keylist.remove(key);
				break;
			}
			else if (buffer.indexOf(content)!=keylist.get(string).getNow()) {
				KeyInfo KeyInfo=new KeyInfo(buffer.indexOf(content), content);
				keylist.put(string, KeyInfo);
				break;
			}
		}
		for (String string : keylist.keySet()) {
			String content=keylist.get(string).getContent();
			if (!buffer.contains(content)) {
				key=string;
				keylist.remove(key);
				break;
			}
			else if (buffer.indexOf(content)!=keylist.get(string).getNow()) {
				KeyInfo KeyInfo=new KeyInfo(buffer.indexOf(content), content);
				keylist.put(string, KeyInfo);
				break;
			}
		}
		for (String string : keylist.keySet()) {
			String content=keylist.get(string).getContent();
			if (!buffer.contains(content)) {
				key=string;
				keylist.remove(key);
				break;
			}
			else if (buffer.indexOf(content)!=keylist.get(string).getNow()) {
				KeyInfo KeyInfo=new KeyInfo(buffer.indexOf(content), content);
				keylist.put(string, KeyInfo);
				break;
			}
		}
		for (String string : keylist.keySet()) {
			String content=keylist.get(string).getContent();
			if (!buffer.contains(content)) {
				key=string;
				keylist.remove(key);
				break;
			}
			else if (buffer.indexOf(content)!=keylist.get(string).getNow()) {
				KeyInfo KeyInfo=new KeyInfo(buffer.indexOf(content), content);
				keylist.put(string, KeyInfo);
				break;
			}
		}
	}
}