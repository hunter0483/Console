package ru.croc.frpo.Console;


import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static void main( String[] args ) {
    	DirectoryOpt dir = new DirectoryOpt();
  //  	Path dirLog = Paths.get(System.getProperty("user.dir")  + "\\logs");
  //  	dir.mkdir(dirLog);

    	
    	System.out.println("Доброе время суток!\nВас приветствует консоль. С помощью неё можно создавать директории,\n"
    			+ "переключаться между ними и выводить их содержание. \n"
    			+ "'cd  (абсолютный путь|относительный путь)'  - перейти в папку\n"
    			+ "'ls [(абсолютный путь|относительный путь)]' - вывод содержимого директории. "
    			+ " (При вводе без параметров показывает содержимое текущей директории) \n"
    			+ "'up' - переход на одну директорию выше"
    			+ "'mkdir (абсолютный путь|относительный путь)' - создать новую директорию.\n"
    			+ "'exit' - выход из консоли.");
    	boolean exit = false; //Переменная содержит информацию о том, была ли подана команда на выход из программы
//    	ArrayList<String> list = new ArrayList<>();
    	Scanner scan = new Scanner(System.in);
    	while(!exit){ //Запрашиваем команды до тех пор, пока не подана команада на выход
    		System.out.print(System.getProperty("user.dir") + ">");
    		String command = scan.nextLine(); //Считываем команду
    		switch(command){//Пытаемся понять, какая команда подана
    		case "ls" : dir.showFiles(); //подана команда распечатаь файлы в текущей директории
//    					list.add("ls");
    					break;
    		case "up":  dir.goUp(); //Подана команда вверх
//    					list.add("up");
    					break;
    		case "exit":exit = true; // подана команда на выход
//    					list.add("exit");
    					break;
    		default :{ //Остальные варианты
    			if (command.length()>3 && command.substring(0, 2).equals("ls")){ //Просят показать файлы по указанному пути
    				String pathS = command.substring(3); // отделяем путь от команды
    				try{ //Проверяем, корректный ли путь
    					Path path = Paths.get(pathS);
    					dir.showFiles(path);//если корректный, показываем файлы
    					break;
    				} catch (InvalidPathException e){ //Если путь некорректный
    					System.out.println("К сожалению, вы ввели недопустимый путь файла.");
    					break;
    				}
    				
    			} else if  (command.length()>3 && command.substring(0, 2).equals("cd")){ //Навигация по директориям, указан путь
    				String pathS = command.substring(3); // Отделяем путь от команды
    				try{ //Проверяем корректность пути
    					Path path = Paths.get(pathS);
    					if (dir.changeDir(path)){//Если удаётся изменить текущую директорию
    						dir.changeDir(path);
    						break;
    					} else { //Если директория не найдена
    						System.out.println("К сожалению,  директория не найдена.");
    						break;
    					}
    				} catch (InvalidPathException e){ //Если путь некорректен
    					System.out.println("К сожалению, вы ввели недопустимый путь файла.");
    					break;
    				}
    			} else if (command.length()>6 &&command.substring(0, 5).equals("mkdir")){ //Конанда создать директорию
    				String pathS = command.substring(6); //Отделяем путь от команды
    				try{//Проверяем корректность пути
    					Path path = Paths.get(pathS);
    					if (dir.mkdir(path)){ //Если можно создать директорию по указанному пути, создаем
    						dir.mkdir(path);
    						break;
    					} else { //Иначе она уже существует
    						System.out.println("Директория уже существует ");
    						break;
    					}
    				} catch (InvalidPathException e){ //Путь некорректен
    					System.out.println("К сожалению, вы ввели недопустимый путь файла.");
    					break;
    				}
    			} else { //Команды нет в списке перечисленных
    				System.out.println("Вы ввели недопустимую команду.");
    			}
    		}//end default
    		}// end switch
    	}//end while
    	 //   	LocalDateTime date = LocalDateTime.now();
    	 //  	String s = date.toString().replaceAll(":", "-");
    	 //   	Path file = Paths.get(System.getProperty("user.dir")  + "\\logs\\log" + s +".txt");

    	 //   	try {
    	 //			Files.createFile(file);
    	 //		} catch (IOException e) {
    	 //			e.printStackTrace();
    	 //		}
    	    	
    	scan.close();
    }
}
