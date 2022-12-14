public class Queue {

    // Выводим сообщение
    // с именем текущего потока в начале.
    static void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    private static class MessageLoop
            implements Runnable {
        public void run() {
            String importantInfo[] = {
                    "Обрабатывается первая очередь покупателей",
                    "Обрабатывается вторая очередь покупателей",
                    "Обрабатывается третья очередь покупателей",
                    "Обрабатывается четвертая очередь покупателей"
            };
            try {
                for (int i = 0;
                     i < importantInfo.length;
                     i++) {
                    Thread.sleep(4000);
                    threadMessage(importantInfo[i]);
                }
            } catch (InterruptedException e) {
                threadMessage("Обработка не закончена");
            }
        }
    }

    public static void main(String args[])
            throws InterruptedException {
        long patience = 1000 * 60 * 60;
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Аргумент должен быть целочисленным");
                System.exit(1);
            }
        }

        threadMessage("Запуск потока MessageLoop");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();

        threadMessage("Ожидание завершения потока MessageLoop");
        while (t.isAlive()) {
            threadMessage("Все еще обрабатывается...");
            t.join(10000);
            if (((System.currentTimeMillis() - startTime) > patience)
                    && t.isAlive()) {
                threadMessage("Истекло время ожидания");
                t.interrupt();
                t.join();
            }
        }
        threadMessage("Обработка закончена!");
    }
}