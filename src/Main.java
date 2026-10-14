
import java.util.Locale;
import java.util.Scanner;

public class Main {

    // Константы игры
    static final int MAX_HP = 40;
    static final int HEAL_AMOUNT = 8;
    static final int XP_REWARD = 20;
    static final int MIN_LEVEL = 10;
    static final int MAX_LEVEL = 80;

    // Вариант А — Руины
    static final String VARIANT = "А — Руины";
    static final int HERO_DAMAGE = 10;

    static final String[] ENEMY_NAMES = {
            "Крыса", "Скелет", "Страж"
    };

    static final int[] ENEMY_HP = {
            12, 18, 24
    };

    static final int[] ENEMY_DAMAGE = {
            4, 6, 8
    };

    // Проверка допуска на арену
    static boolean checkAdmission(
            String heroClass, int level, boolean hasShield) {

        return level >= MIN_LEVEL
                && (hasShield || heroClass.equals("маг"));
    }

    // Расчёт HP после получения урона
    static int calculateDamage(int currentHp, int damage) {
        return Math.max(0, currentHp - damage);
    }

    // Расчёт HP после лечения
    static int calculateHealing(int currentHp) {
        return Math.min(MAX_HP, currentHp + HEAL_AMOUNT);
    }

    // Итоговая сводка
    static void printSummary(
            String heroName,
            int victories,
            int xp,
            int heroHp,
            String result) {

        System.out.println("\n========== ИТОГ АРЕНЫ ==========");
        System.out.println("Герой: " + heroName);
        System.out.println("Вариант: " + VARIANT);
        System.out.println("Побед: " + victories);
        System.out.println("Опыт: " + xp + " XP");
        System.out.println("Здоровье: " + heroHp + "/" + MAX_HP);
        System.out.println("Итог: " + result);
        System.out.println("================================");
    }

    // Чтение строки с обработкой конца ввода
    static String readLine(Scanner scanner, String message) {
        System.out.print(message);

        if (!scanner.hasNextLine()) {
            return null;
        }

        return scanner.nextLine();
    }

    // Ввод целого числа с повторным запросом
    static Integer readInteger(
            Scanner scanner, String message) {

        while (true) {
            String line = readLine(scanner, message);

            if (line == null) {
                return null;
            }

            line = line.strip();

            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Ошибка: введите целое число.");
            }
        }
    }

    // Регистрация героя
    static String registerHeroName(Scanner scanner) {
        while (true) {
            String name = readLine(
                    scanner, "Введите имя героя: ");

            if (name == null) {
                return null;
            }

            name = name.strip();

            if (name.length() >= 1 && name.length() <= 30) {
                return name;
            }

            System.out.println(
                    "Имя должно содержать от 1 до 30 символов.");
        }
    }

    static String registerHeroClass(Scanner scanner) {
        while (true) {
            String heroClass = readLine(
                    scanner,
                    "Введите класс (воин, маг, лучник): ");

            if (heroClass == null) {
                return null;
            }

            heroClass = heroClass.strip()
                    .toLowerCase(Locale.ROOT);

            if (heroClass.equals("воин")
                    || heroClass.equals("маг")
                    || heroClass.equals("лучник")) {
                return heroClass;
            }

            System.out.println(
                    "Неизвестный класс. Повторите ввод.");
        }
    }

    static Integer registerLevel(Scanner scanner) {
        while (true) {
            Integer level = readInteger(
                    scanner, "Введите уровень (1–80): ");

            if (level == null) {
                return null;
            }

            if (level >= 1 && level <= MAX_LEVEL) {
                return level;
            }

            System.out.println(
                    "Уровень должен быть от 1 до 80.");
        }
    }

    static Boolean registerShield(Scanner scanner) {
        while (true) {
            String answer = readLine(
                    scanner, "Есть ли щит? (да/нет): ");

            if (answer == null) {
                return null;
            }

            answer = answer.strip()
                    .toLowerCase(Locale.ROOT);

            if (answer.equals("да")) {
                return true;
            }

            if (answer.equals("нет")) {
                return false;
            }

            System.out.println(
                    "Введите только «да» или «нет».");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== АРЕНА НОВИЧКА ===");
        System.out.println("Регистрация героя\n");

        String heroName = registerHeroName(scanner);

        if (heroName == null) {
            System.out.println("Ввод завершён. Регистрация прервана.");
            return;
        }

        String heroClass = registerHeroClass(scanner);

        if (heroClass == null) {
            System.out.println("Ввод завершён. Регистрация прервана.");
            return;
        }

        Integer level = registerLevel(scanner);

        if (level == null) {
            System.out.println("Ввод завершён. Регистрация прервана.");
            return;
        }

        Boolean hasShield = registerShield(scanner);

        if (hasShield == null) {
            System.out.println("Ввод завершён. Регистрация прервана.");
            return;
        }

        System.out.println("\nПроверка допуска...");

        // Сначала проверяем уровень
        if (level < MIN_LEVEL) {
            System.out.println(
                    "Отказ: уровень героя должен быть не ниже 10.");
            return;
        }

        // Затем проверяем защиту
        if (!hasShield && !heroClass.equals("маг")) {
            System.out.println(
                    "Отказ: воину и лучнику необходим щит.");
            return;
        }

        if (!checkAdmission(heroClass, level, hasShield)) {
            System.out.println("Отказ в допуске.");
            return;
        }

        System.out.println(
                "Допущен: " + heroName + ", " + heroClass);
        System.out.println("Вариант: " + VARIANT);
        System.out.println("Сила атаки героя: " + HERO_DAMAGE);

        // Состояние героя сохраняется между боями
        int heroHp = MAX_HP;
        int xp = 0;
        int victories = 0;

        String result = "Арена пройдена";
        boolean gameOver = false;

        // Три боя подряд
        for (int i = 0; i < ENEMY_NAMES.length; i++) {

            int enemyHp = ENEMY_HP[i];
            boolean healingUsed = false;

            System.out.println("\n========== РАУНД "
                    + (i + 1) + " ==========");
            System.out.println(
                    "Противник: " + ENEMY_NAMES[i]);

            // Ходы в текущем бою
            while (enemyHp > 0 && heroHp > 0) {

                System.out.println("\n--------------------");
                System.out.println(
                        heroName + ": HP " + heroHp + "/" + MAX_HP);
                System.out.println(
                        ENEMY_NAMES[i] + ": HP " + enemyHp
                                + "/" + ENEMY_HP[i]);

                System.out.println("1 — Атаковать");
                System.out.println("2 — Лечиться");
                System.out.println("0 — Завершить игру");

                String command = readLine(
                        scanner, "Ваш ход: ");

                if (command == null) {
                    System.out.println(
                            "\nВвод завершён. Игра прервана.");
                    result = "Игра прервана";
                    gameOver = true;
                    break;
                }

                command = command.strip();

                switch (command) {
                    case "1":
                        // Герой атакует
                        enemyHp = calculateDamage(
                                enemyHp, HERO_DAMAGE);

                        System.out.println(
                                heroName + " наносит "
                                        + HERO_DAMAGE + " урона.");

                        // Побеждённый враг не отвечает
                        if (enemyHp == 0) {
                            System.out.println(
                                    ENEMY_NAMES[i] + " побеждён!");
                        } else {
                            // Ответная атака врага
                            heroHp = calculateDamage(
                                    heroHp, ENEMY_DAMAGE[i]);

                            System.out.println(
                                    ENEMY_NAMES[i]
                                            + " наносит "
                                            + ENEMY_DAMAGE[i]
                                            + " урона.");

                            if (heroHp == 0) {
                                System.out.println(
                                        "Здоровье героя закончилось.");
                                result = "Поражение";
                                gameOver = true;
                            }
                        }
                        break;

                    case "2":
                        if (healingUsed) {
                            System.out.println(
                                    "Лечение уже использовано в этом бою.");
                            break;
                        }

                        // Лечение расходует ход
                        heroHp = calculateHealing(heroHp);
                        healingUsed = true;

                        System.out.println(
                                "Герой восстановил здоровье: "
                                        + heroHp + "/" + MAX_HP);

                        // Противник отвечает после лечения
                        heroHp = calculateDamage(
                                heroHp, ENEMY_DAMAGE[i]);

                        System.out.println(
                                ENEMY_NAMES[i]
                                        + " наносит "
                                        + ENEMY_DAMAGE[i]
                                        + " урона.");

                        if (heroHp == 0) {
                            System.out.println(
                                    "Здоровье героя закончилось.");
                            result = "Поражение";
                            gameOver = true;
                        }
                        break;

                    case "0":
                        result = "Игра прервана";
                        gameOver = true;
                        System.out.println(
                                "Вы покинули арену.");
                        break;

                    default:
                        System.out.println(
                                "Неизвестная команда. "
                                        + "Введите 1, 2 или 0.");
                        break;
                }

                // Выход из всей арены, а не только из боя
                if (gameOver) {
                    break;
                }
            }

            if (gameOver) {
                break;
            }

            // Опыт начисляется один раз после победы
            if (enemyHp == 0) {
                victories++;
                xp += XP_REWARD;

                System.out.println("\nПобеда в раунде "
                        + (i + 1) + "!");
                System.out.println("Получено XP: "
                        + XP_REWARD);
                System.out.println("Всего XP: " + xp);
                System.out.println("HP героя: " + heroHp);
            }
        }

        if (!gameOver && victories == ENEMY_NAMES.length) {
            result = "Арена пройдена";
        }

        printSummary(
                heroName,
                victories,
                xp,
                heroHp,
                result
        );

        scanner.close();
    }
}
