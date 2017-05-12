package ru.croc.frpo.Console;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Данный класс является точкой входа для программы консоли.
 * Программа консоли презназначена для разных консольных операций:
 * создания файлов и директоирй, отобржаения содержимого файлов и директорий,
 * навигации по директориям, добавления содержимого в файл. Программа может как
 * работать в режиме реального времени, так и считывать команды из файла.
 * @author Елизавета Охота
 *
 */
public class App {
	
    public static void main( String[] args ) {

    	LocalDateTime date = LocalDateTime.now();
    	int hour = date.getHour();
    	String hello = null;
    	if (hour > 5 && hour < 12 ){
    		hello = "Доброе утро!";
    	} else if (hour > 11 && hour < 18){
    		hello = "Добрый день!";
    	} else if (hour > 17 ){
    		hello = "Добрый вечер!";
    	} else if (hour < 6){
    		hello = "Доброй ночи!";
    	}
    	String beginDirectory = System.getProperty("user.dir");
    	System.out.println( hello +" Вас приветствует консоль. \n\n"
    			+ "'cd  (<абсолютный путь>|<относительный путь>)'  - перейти в директорию.\n\n"
    			+ "'ls [(<абсолютный путь>|<относительный путь>)]' - вывод содержимого директории.\n "
    			+ "При вводе без параметров показывает содержимое текущей директории. \n\n"
    			+ "'..' - переход на одну директорию выше.\n\n"
    			+ "'mkdir (<имя директории>)' - создать новую директорию в текущей директории.\n\n"
    			+ "'create <имя файла>'  -  создать в текущей директории файл с данным именем. \n\n"
    			+ "'append (<абсолютный путь>|<относительный путь>)' - добавить информацию в файл.\n"
    			+ "После вызова этой команды пользователю предлагается ввести текст, который он хочет добавить в файл.\n"
    			+ "Для завершения ввода текста в файл требуется ввести команду 'exitAppend'\n\n"
    			+ "'show <число строк> (<абсолютный путь>|<относительный путь>)' - показать заданное количество строк у выбранного файла. \n\n"
    			+ "'exit' - выход из консоли.\n\n");


    	/*
    	 * Определяем систему, в которй запускаем через консоль программу.
    	 * Если программа запущена на Windows, изменяет кодировку на cp866 (кодировка консоли Windows).
    	 * Иначе - UTF-8
    	 */
    	String OS = System.getProperty("os.name").toLowerCase();
    	String encoding;
    	if (OS.contains("win")){
    		encoding = "cp866";
    	} else {
    		encoding = "UTF-8";
    	}
	 	Scanner scan = new Scanner(System.in, encoding);
//    	Scanner scan = new Scanner(System.in); //Эта строка для компиляции в среде
    	/*
    	 * Если в качестве агрумента командно строки подан аргумент, значит считывать команды будем
    	 * из файла. Если программа запущена без аргументов - значит, запускаем программу
    	 * в реальном времени.
    	 */
	 	String option;
    	String fileCommands = null;
    	if (args.length > 0){
    		fileCommands = args[0];
    		option = "file";
    	} else {
    		option = "realtime";
    	}
    	Scanner scanCommands = null;
    	/*
    	 * Если считываем из файла, 
    	 * ищем в директории logs файл с нужным именем, 
    	 * созаём Scanner, который читает из этого файла. 
    	 */
		if (option.equals("file")){
			try {
				Path p = absPath(Paths.get(fileCommands));
				if (Files.notExists(p)){
					System.out.println("Файл с командами не найден. Программа завершает работу.");
					scan.close();
					return;
				}
				scanCommands = new Scanner(Files.newInputStream(p),"UTF-8");
			} catch (InvalidPathException e){
				System.out.println("Вы ввели недопустимый путь. Программа завершает работу.");
				scan.close();
				return;
			} catch (IOException ex){
				System.out.println("Произошла ошибка считывания файла. Программа завершает работу.");
			}
		/*
//		 * Если считываем с клавиатуры, то Scanner менять не надо.
		 */
		} else if (option.equals("realtime")){  
			scanCommands = scan;
		/*
		 * Если введена недопустимая команда,
		 * завершаем работу программы.
		 */
		} else {
			System.out.println("Вы ввели недопустимую команду. Программа завершит работу.");
			scan.close();
			return;
		}
		/*
		 * Выполняем команды, сохраняем результат в виде списка.
		 * Затем создаём файл log(yyyy.mm.dd.hh.ss.ms).txt в который хотим сохранить лог исполнения программы,
		 * в папке logs в дирекории исполнения программы.
		 * Если папка logs отсутвует в директории запуска, создаём ее.
		 */
		List<String> commands = scanCommands(scanCommands, option);
		if (option.equals("realtime")){
			String s = date.toString().replaceAll(":", "-");
		    Path file = Paths.get(beginDirectory  + "\\logs\\log" + s +".txt");
		    try {
		       	Path dirLog = file.getParent();
		       	if (!Files.exists(dirLog)){
		       		Files.createDirectory(dirLog);
		       	}
				Files.createFile(file);
				Files.write(file, commands);
				System.out.println("Файл с выполненными командами\n " + file.toString() + "\nбыл успешно создан.");
			} catch (IOException e) {
				System.out.println("К сожалению, файл с коммандами не был сохранён, произошла ошибка записи.");
			}	    	
		}
	    scan.close();
	    scanCommands.close();
	}

	/**
	 * Если переданный путь не абсолютный, метод делает его абсолютным.
	 * Если переданный путь абсолютный, не изменяет его.
	 * Этот метод отличается от Path.getAbsolute() тем, что создаёт абсолютный путь относительно текущей директории программы консоли.
	 * @param path - Путь, для которого хотим получить абсолютный.
	 * @return Возвращает абсолютный путь переданного пути.
	 */
	public static Path absPath(Path path) {
		if (!path.isAbsolute()){
			String s = System.getProperty("user.dir") + "\\" + path.toString();
			path = Paths.get(s);
		}
		return path;
	}
	
	/**
	 * Этот метод, используя Scanner, принимает на вход консольные команды, выполняет их
	 * и сохраняет в список строк.
	 * @param scan - Отсюда сканируются команды
	 * @param option - "file" - Если сканируем из файла
	 * "realtime" - если сканируем в реальном времени. 
	 * @return Возвращает список строк-команд, поданых на вход.
	 */
	public static List<String> scanCommands(Scanner scan, String option) {
    	/*
    	 * Переменная exit содержит информацию о том, 
    	 * была ли подана команда на выход из программы
    	 */
		boolean exit = false;
		List<String> commands = new ArrayList<>();
    	/*
    	 * Запрашиваем команды до тех пор, 
    	 * пока не подана команада на выход
    	 */
    	while(!exit){ 
    		System.out.print(System.getProperty("user.dir") + ">");
    		
    		/*
    		 * Считываем команду и проверяем, совпадает ли она
    		 * с командами из числа тех, которые способна выполнить программа.
    		 */
    		if (scan.hasNext()){
        		String command = scan.nextLine(); 
            	commands.add(command);
            	if (option.equals("file")){
            		System.out.println(command);
            	}
            	try {
            		switch(command){
            		/*
            		 * Дана команда распечатаь файлы в текущей директории
            		 */
            		case "ls" : DirectoryOpt.showFiles(); 
            					break;
            		/*
            		 * Дана команда вверх
            		 */
            		case "..":  DirectoryOpt.goUp(); 
            					break;
            		/*
            		 *  Дана команда на выход
            		 */
            		case "exit":exit = true; 
            					System.out.println("Консоль завершает работу.");
            					break;
            		/*
            		 * Рассматриваем остальные варианты
            		 */
            		default :{
            		/*
            		 * Команда показать файлы по указанному пути.
            		 */
            		if (command.length()>3 && command.substring(0, 2).equals("ls")){ 
            			ConsoleParser.parseCdCommand(command);
            			break;
            		} 
              		/*
            		 * 	Команда изменить текущую директорию
            		 */
            		else if  (command.length()>3 && command.substring(0, 2).equals("cd")){ 
            			ConsoleParser.parseCdCommand(command);
            			break;
            		} 
            		/*
            		 * Команда создать новую директорию
            		 */
            		else if (command.length()>6 && command.substring(0, 5).equals("mkdir")){ 
            			ConsoleParser.parseMkdirCommand(command);
            			break;
            		} 
            		/*
            		 * Команда добавить строки в файл
            		 */
            		else if (command.length()>7 && command.substring(0, 6).equals("append")){
            			ConsoleParser.parseAppendCommand(command, scan);
            			break;
            		}
            		/*
            		 * Команда показать первые n строк у заданного файла
            		 */
            		else if(command.length()>5 && command.substring(0,4).equals("show")){
            			ConsoleParser.parseShowCommand(command);
            			break;
            		} 
            		/*
            		 * Команда создать нвоый файл.
            		 */
            		else if (command.length()>7 && command.substring(0,6).equals("create")){
            			ConsoleParser.parseCreateCommand(command);
            			break;
            		}
            		/*
            		 * Команды нет в списке перечисленных
            		 */
            		else { 
            			System.out.println("Вы ввели недопустимую команду.");
            		}
            		
            		}//end default
            		
            		}// end switch
            		
            		} catch (InvalidPathException ex){
            			System.out.println("Вы ввели недопустимый путь или недопустимое имя файла.");
            		} catch (IOException ex) {
            			System.out.println("Произошла ошибка доступа.");
            		} 
    		} else {
    			exit = true;
    			break;
    		}	
    	}//end while
    	return commands;
	}
	
}
