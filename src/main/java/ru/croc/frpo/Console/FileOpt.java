package ru.croc.frpo.Console;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Этот класс содержит статические методы, совершающие операции
 * над файлами. 
 * @author Елизавета Охота
 *
 */
public class FileOpt {
	

	/**
	 * Метод создаёт новый файл в текущей директории.
	 * @param name Имя файла, который хотим создать.
	 * @return 0 - если файл успешно создан
	 * 1 - если имя файла включает иерархию директорий
	 * 2 - если директория уже существует.
	 * @throws IOException
	 * @throws InvalidPathException
	 */
	public static int newFile(String name) throws IOException, InvalidPathException{
		/*
		 * Если название файла включает иерархию директорий
		 */
		if (name.contains("\\")) {
			return 1;
		}
		Path path = Paths.get(name); //throws InvalidPathException
		Path file = App.absPath(path);
		/*
		 * Если дирекотрия не существует, создаём ее.
		 */
		if(!Files.exists(file)){
			file = Files.createFile(file); //throws IOException
			return 0;
		} else {
			return 2; //Если директория уже существует
		}
	}
	
	/**
	 * Метод добавляет в файл с заданным именем список строк.
	 * @param name Имя файла, в который надо добавить строки.
	 * @param lines Строки, которые надо добавить в файл
	 * @throws IOException 
	 * @throws InvalidPathException
	 */
	public static void appendFile(String name, List<String> lines) throws IOException, InvalidPathException{
		Path file = App.absPath(Paths.get(name));  //throws InvalidPathException
		/*
		 * Если файл, в который хотим добавить строки, не существует, создаём его.
		 */
		Files.write(file, lines); //throws IOException
	}

	
	/**
	 * Метод читает заданное количество строк из файла и возвращает список прочитанных строк.
	 * Если в файле, поданном на вход, оказалось меньше или равно строк, чем требуется, возвращает их все.
	 * @param pathS Путь файла, может быть как абсолютный, так и относительный.
	 * @param n - количество строк, которые нужно прочитать.
	 * @return Возвращает список из первых n  строк файла. 
	 * Если файла не существует, возвращает null.
	 * Если файл существует, но пустой, возвращает пустой список строк.
	 * @throws IOException
	 * @throws InvalidPathException
	 */
	public static List<String> readFileLines(String pathS, int n)  throws IOException, InvalidPathException{
		/*
		 * Находим абсолютный путь к файлу
		 */
		Path file = App.absPath(Paths.get(pathS));
		/*
		 * Если файл не существует, возвращаем null
		 */
		if (Files.notExists(file)){
			return null;
		}
		/*
		 * Помещаем все строки из файла в список строк
		 */
		List<String> result =  Files.readAllLines(file);//throws IOException
		int length = result.size();
		/*
		 * Если в файле оказалось меньше, чем n строк, возвращаем
		 * список со всеми строками файла. Иначе укроачичваем итоговй список так,
		 * чтобы в нём осталось ровно n строк.
		 */
		if(length <= n){
			return result;
		} else {
			for (int i = length-1; i >= n; i--){
				result.remove(i);
			}
			return result;
		}
	}
	
	/**
	 * Печатает первые n строк из заданного файла. Если в файле строк меньше, чем требуется распечатать, 
	 * печатает все строки файла.
	 * @param pathS Путь к файлу, из которого надо напечатать строки, абсолютный или относительный.
	 * @param n Количество строк, которое нужно распечатать. 
	 * @throws InvalidPathException
	 * @throws IOException
	 */
	public static void printFileLines(String pathS, int n) throws InvalidPathException, IOException{
		List<String> list = readFileLines(pathS, n);
		if (list !=null ){
			if (list.isEmpty()){
				System.out.println("Файл является пустым.");
			}
			int i = 1;
			for (String line : list){
				System.out.println(i + ": " + line);
				i++;
			}
		} else {
				System.out.println("Такого файла не существует.");
		}
	}
}
