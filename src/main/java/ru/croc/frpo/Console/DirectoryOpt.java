package ru.croc.frpo.Console;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Данный класс содержит методы для работы с директориями в консоли.
 * @author Елизавета Охота
 *
 */
public class DirectoryOpt {
	
	/**
	 * Этот метод создаёт директорию по указанному пути. 
	 * @param path - Путь к новой директории. Может быть как абсолютный, так и относительный.
	 * @return Если новая директория создана, возвращает true. Если такая директория уже существует
	 * и создана не была, возвращает false.
	 */
	public boolean mkdir(Path path) {
		File folder = absPath(path);
		/*
		 * Если дирекотрия не существует, создаём ее.
		 */
		if(!folder.exists()){
			folder.mkdir();
			return true;
		} else{
			return false;
		}
	}

	/**
	 * Этот метод возвращает массив файлов в директории по указанному пути.
	 * @param path - Путь к директории, из которой читаем файлы. Может быть как абсолютный, так и относительный.
	 * @return Возвращает массив файлов, находящихся в данной директории. Если директория пустая - пустой массив.
	 * Если директории не существует - null.
	 */
	File[] listFiles(Path path) {
		File folder = absPath(path);
		File[] list = folder.listFiles();
		return list;
	}

	/**
	 * Этот метод печатает массив файлов, указывая, директория это, или файл.
	 * @param list - Массив файлов для печати. Сообщит, если файлы отсутствуют или если директория не существует.
	 */
	void printFiles(File[] list){
		if (list != null){
			if (list.length == 0){
				System.out.println("В этой директории отсутствуют файлы.");
			}
			for (File f : list){
				if(f.isDirectory()){
					System.out.print("Директория: ");
				} else{
					System.out.print("Файл:       ");
				}
				System.out.println(f.getName());
			}
		} else{
			/*
			 * Метод listFiles может возвращать значение null, если директории не существует.
			 * Учитываем это.
			 */
			System.out.println("К сожалению, такой директории не существует.");
		}
	}

	/**
	 * Этот метод возвращает массив файлов в корневой директории.
	 * @return Возвращает массив файлов, находящийся в директории.
	 */
	File[] listFiles() {
		Path path = Paths.get(System.getProperty("user.dir"));
		return listFiles(path);
	}
	
	/**
	 * Распечатывает файлы, располагающиеся в данной директории.
	 * @param path - Директория, из которой надо печатать файлы.
	 */
	public void showFiles(Path path){
		printFiles(listFiles(path));
	}
	
	/**
	 * Распечатывает файлы корневой директории.
	 */
	public void showFiles(){
		printFiles(listFiles());
	}

	/**
	 * Изменяет корневую директорию на заданную.
	 * @param path - Новая корневая директория. Как абсолютный, так и относительный путь.
	 * @return Если директория существует, возвращает true. Иначе false.
	 */
	public boolean changeDir(Path path) {
		File file = absPath(path);
		Path abs= file.toPath();
		if (file.exists()){
			System.setProperty("user.dir", abs.toString());
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Перемещает корневую директорию на один шаг вверх (к родительской директории).
	 * @return Возващает true, если корневая директория успешно перемещена.
	 * Возвращает false, если директории выше не существует.
	 */
	public boolean goUp() {
		Path directory = Paths.get(System.getProperty("user.dir"));
		Path parent = directory.getParent();
		if (parent == null) {
			System.out.println("У текущей директории нет родительской директории.");
			return false;
		} else {
			System.setProperty("user.dir", parent.toString());
			return true;
		}
	}
	
	/**
	 * Если переданный путь не абсолютный, делает его абсолютным и создаёт новый файл, относящийся к этому пути.
	 * Если переданный путь абсолютный, просто создаёт относящийся к нему файл.
	 * @param path - Путь, для которого хотим получить абсолютный.
	 * @return Возвращает файл, относящийся к полученному абсолютному пути.
	 */
	File absPath(Path path){
		File folder;
		if (path.isAbsolute()){
			folder = path.toFile();
		} else{
			String s = System.getProperty("user.dir") + "\\" + path.toString();
			Path abs = Paths.get(s);
			folder = abs.toFile();
		}
		return folder;
	}
}
