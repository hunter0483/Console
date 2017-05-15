package ru.croc.frpo.Console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Этот класс содержит статические методы, предназначенные для расшифровки
 * команд, поданных в консоль, и их последующего выполнения.
 * @author Елизавета Охота
 *
 */
public class ConsoleParser {
	private final static char SPACEBAR = (char) 0x0020; //константа-пробел
	
	/**
	 * Метод расшифровывает и выполняет команду печати n строк из файла
	 * с помощью метода FileOpt.printFileLines(String string, int n);
	 * @param command - команда, полученная из консоли.
	 * @throws IOException
	 */
	public static void parseShowCommand(String command) throws IOException{
		/*
		 * Отделяем строку, в которой содержатся параметры команды show.
		 */
		String string = command.substring(5);
		/*
		 * Извлекаем число, находящееся в начале строки с параметрами команды.
		 * Если строка начинается не с числа, завершает работу. 
		 */
		Pattern number = Pattern.compile("[0-9]+");
		Matcher match = number.matcher(string);
		boolean found = match.find();
		if (!found || match.start() != 0){
			System.out.println("Вы ввели недопустимую команду. После команды show должно следовать число.");
			return;
		}
		int n = Integer.parseInt(match.group());
		/*
		 * Извлекаем строку, идущую после числа n.
		 * Если после числа не следует пробел, значит команда введена не верна,
		 * метод завершает работу. 
		 */
		string = string.substring(match.end());
		if (string.charAt(0) != SPACEBAR){
			System.out.println("Недопустимая команда. Число и путь к файлу должны быть разделен пробелом."); 
			return;
		}
		/*
		 * Извлекаем строку, содержащую команду. 
		 * Печатаем метододом printFileLines первые n строк файла.
		 */
		string = string.substring(1);
		FileOpt.printFileLines(string, n);
	}
	
	/**
	 * Метод расшифровывает команду изменения текущей директории cd на новую директорию
	 * с помощью метода  DirectoryOpt.changeDir(String pathS)
	 * @param command - Команда, которую нужно выполнить
	 */
	public static void parseCdCommand(String command){
		String pathS = command.substring(3); 
		boolean res = DirectoryOpt.changeDir(pathS);
		if (res){ //Если удаётся изменить текущую директорию
			System.out.println("Текущая директория успешно изменена на " + pathS);
		} else { //Если директория не найдена
			System.out.println("К сожалению,  директория не найдена.");
		}
	}
	/**
	 * Метод расшифровывает и выполняет команду вывода на экран файлов ls директории 
	 * с помощью метода DirectoryOpt.showFiles(String path)
	 * @param command - команда, которую нужно расшифровать и выполнить
	 * @throws IOException
	 */
	public static void parseLsCommand(String command) throws  IOException{
		String pathS = command.substring(3); // отделяем путь от команды
		DirectoryOpt.showFiles(pathS);//показываем файлы
	}

	/**
	 * Метод расшифровывает и выполняет команду создания новой директории mkdir
	 * с помощю метода DirectoryOpt.mkdir(String path).
	 * @param command - Команда, которую нужно расшифрвоать и выполнить
	 * @throws IOException
	 */
	public static void parseMkdirCommand(String command) throws  IOException{
		String pathS = command.substring(6); //Отделяем путь от команды
		int codeExit = DirectoryOpt.mkdir(pathS);
		if (codeExit == 0){ //Если можно создать директорию по указанному пути, все  порядке
			System.out.println("Директория успешно создана.");
		} else if (codeExit == 1){ 
			System.out.println("Можно создать директорию только в текущей папке.");
		} else {
			System.out.println("Директория уже существует.");
		}
	}
	
	/**
	 * Метод расшифровывает и выполняет команду создания нового файла create
	 * с помощью метода FileOpt.newFile(String name)
	 * @param command - команда, которую нужно расшифровать и выполнить
	 * @throws IOException
	 */
	public static void parseCreateCommand(String command) throws IOException{
		String pathS = command.substring(7);
		int code = FileOpt.newFile(pathS);
		if (code == 0 ){
			System.out.println("В текущей директории создан новый файл "+ pathS);
		} else if (code == 1){
			System.out.println("Можно создать файл только в текущей папке.");
		} else {
			System.out.println("Файл " +pathS + "  уже существует.");
		}
		
	}
	
	/**
	 * Метод расшифровывает и выполняет команду добавления текстовых данных в файл
	 * append с помощью метода FileOpt.appendFile(String pathS, List<String> lines);
	 * @param command - команда, которую нужно расшифровать и выполнить
	 * @param scan - Сканнер, откуда считываются данные для добавления в файл.
	 * @throws IOException
	 */
	public static List<String> parseAppendCommand(String command, Scanner scan) throws IOException{
		/*
		 * Отделяем путь к файлу от команды.
		 */
		String pathS = command.substring(7);
		/*
		 * Считываем строки, которые хотим запиать в файл и 
		 * добавляем их в список строк.
		 */
		boolean exitAppend = false;
		List<String> lines = new ArrayList<>();
		while (!exitAppend){
			if (scan.hasNext()){
				String line = scan.nextLine();
				if (!line.equals("exitAppend")){
					lines.add(line);
				} else {
					exitAppend = true;
					
				}
			} else {
				break;
			}

		}
		/*
		 * Записываем список строк в файл.
		 */
		FileOpt.appendFile(pathS, lines);
		System.out.println("В файл " + pathS +  "  успешно добавлены строки.");
		return lines;
	}
}
