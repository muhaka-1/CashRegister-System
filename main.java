import java.util.Scanner;
import java.util.Random;
import java.util.Date;

public class Main {
    public static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
      
      // declaring array(database)
        Random random = new Random();
        int upperBound = 10;
        int[][] articles = null;
        int[][] sales = new int[1000][3];
        Date[] salesDate = new Date[1000]; 
      
      //Printing array (database)
        articles = new int[10][3];
        for (int i = 0; i < articles.length; i++) {
            articles = insertArticles(articles,i + 1000, random.nextInt(upperBound) + 1);
        }
         System.out.println("Artnr, Artunt  , Prise");
        printArray(articles);

        //Menu Method
        boolean run = true;
        while (run) {
            int theChoice = menu();
            int articleNumber, noOfArticles;
            switch (theChoice) {
                case 1:
                    System.out.println("You selected \"Insert Articles\"");
                    System.out.print("Article number ? : ");
                    articleNumber = userInput.nextInt();
                    System.out.print("Article quantity ? : ");
                    noOfArticles = userInput.nextInt();
                    articles = insertArticles(articles, articleNumber, noOfArticles);
                    break;
                case 2:
                    System.out.println("You selected \"Remove product\"");
                    articles = removeArticle(articles);
                    break;
                case 3:
                    System.out.println("You selected \"View product\"");
                    printArray(articles);
                    break;
                case 4:
                    System.out.println("You selected \"Sales\"");
                    articles = sellArticle(sales, salesDate, articles);
                    break;
                case 5:
                    System.out.println("You selected \" Order history\"");
                    printSales(sales, salesDate);
                    break;
                case 6:
                    System.out.println("You selected \"Sort order history table\"");
                    sortedTable(sales, salesDate);
                    break;
                case 7:
                    System.out.println("You selected \"Exit\"");
                    run = false;
                    break;
                default:
                    System.out.println("Select something in the menu, a number between 1-7");
                    break;
            }
        }
    }

    //Printing menu
    public static int menu() {
        System.out.println("1. Insert Articles");
        System.out.println("2. Remove product");
        System.out.println("3. View product");
        System.out.println("4. Sales");
        System.out.println("5. Order history");
        System.out.println("6. Sort order history table");
        System.out.println("7. Exit");
        System.out.print("Your choice: ");
        // NOTE! We can NOT close the keyboard here as we will probably use the input more times
        return userInput.nextInt();
    }

  // Method for inserting articles
    public static int[][] insertArticles(int[][] articles, int articleNumber, int noOfArticles) {
        final int MAX_PRICE = 1000, MIN_PRICE = 100; 
        int nextInsertionPosition = getNextInsertionPosition(articles, articleNumber);
        int existingArticlePosition = getArticleIndex(articles, articleNumber);

        if (existingArticlePosition == -1) {
            articles = checkFull(articles, noOfArticles);
            if (articles[nextInsertionPosition][0] != 0) {
                articles = shiftRight(articles, nextInsertionPosition);
            }
        } else {
            nextInsertionPosition = existingArticlePosition;
        }

        articles[nextInsertionPosition][0] = articleNumber;
        articles[nextInsertionPosition][1] += noOfArticles;
        articles[nextInsertionPosition][2] = (articles[nextInsertionPosition][2] == 0) ? (int) (Math.random() * ( (MAX_PRICE - MIN_PRICE) + 1) ) + MIN_PRICE : 
          articles[nextInsertionPosition][2];
        return articles;
    }

  // Method for removeing articles
    public static int[][] removeArticle(int[][] articles) {
        System.out.print("Article number ? : ");
        int articleNumber = userInput.nextInt();
        int articleIndex = getArticleIndex(articles, articleNumber);
        if (articleIndex == -1) {
            System.out.println("We don't have this article");
        } else {
            articles[articleIndex][0] = 0;
            articles[articleIndex][1] = 0;
            articles[articleIndex][2] = 0;
        }
        return articles;
    }

  // Method for Selling articles
    public static int[][] sellArticle(int[][] sales, Date[] salesDate, int[][] articles) {
        System.out.print("Article number ? : ");
        int articleNumber = userInput.nextInt();
        System.out.print("Article Quantity ? : ");
        int quantity = userInput.nextInt();
        int articleIndex = getArticleIndex(articles, articleNumber);

        if (articleIndex == -1) {
            System.out.println("We don't have this article");
        } else {
            if (articles[articleIndex][1] < quantity) {
                System.out.println("We don't have enough articles to sell");
            } else {
                articles[articleIndex][1] -= quantity;
                int salesPosition = getNextInsertionPosition(sales, articleNumber);
                if (sales[salesPosition][0] != 0) {
                    sales = shiftRight(sales, salesPosition);
                    salesDate = salesDatesShiftRight(salesDate, salesPosition);
                }
                sales[salesPosition][0] = articleNumber;
                sales[salesPosition][1] = quantity;
                sales[salesPosition][2] = articles[articleIndex][2] * quantity;
                salesDate[salesPosition] = new Date();
            }
        }
        return articles;
    }

    private static void printArray(int[][] toPrint) {
        for (int i = 0; i < toPrint.length; i++) {
            System.out.println(" ( " + toPrint[i][0] + ", " + toPrint[i][1] + ", " + toPrint[i][2] + ") ");
        }
    }

    // https://www.geeksforgeeks.org/search-insert-position-of-k-in-a-sorted-array/
    private static int getNextInsertionPosition(int[][] array, int firstColumnValue) {
        /***
         * Since the first value can never be 0, so, we can determine if a search is necessary or not from the first values
         */
        if (array[0][0] == 0) {
            return 0;
        }

        int start = 0, end = array.length - 1;
        // finding the actual end of data
        while (array[end][0] == 0) {
            end--;
        }

        while (start <= end) {
            int mid = (start + end) / 2;
            if (array[mid][0] == firstColumnValue)
                return mid;
            else if (array[mid][0] < firstColumnValue)
                start = mid + 1;
            else
                end = mid - 1;
        }
        return end + 1;
    }

    /***
     * Shift all the elements of the array starting from index position to array.length to one position right
     * @param array
     * @param index
     * @return
     */
    private static int[][] shiftRight(int[][] array, int index) {
        if (array.length - 1 <= index) {
            return array;
        }

        for (int i = array.length - 2; i >= index; i--) {
            array[i + 1][0] = array[i][0];
            array[i + 1][1] = array[i][1];
            array[i + 1][2] = array[i][2];
        }

        array[index][0] = 0;
        array[index][1] = 0;
        array[index][2] = 0;

        return array;
    }

    private static Date[] salesDatesShiftRight(Date[] array, int index) {
        if (array.length - 1 <= index) {
            return array;
        }
        for (int i = array.length - 2; i >= index; i--) {
            array[i + 1] = array[i];
        }
        array[index] = null;
        return array;
    }

    // Binary Search : https://www.baeldung.com/java-binary-search
    private static int getArticleIndex(int[][] articles, int articleNo) {
        int position = -1;
        int start = 0, end = articles.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (articles[mid][0] == articleNo) {
                return mid;
            } else if (articles[mid][0] > articleNo) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return position;
    }

    public static int[][] checkFull(int[][] articles, int noOfArticles) {
        int currentLength = articles.length;
        int newItemCapacity = 0;
        for (int i = currentLength - 1; i >= 0; i--) {
            if (articles[i][0] == 0) {
                newItemCapacity++;
            } else {
                break;
            }
        }
        if (newItemCapacity == 0) {
            int[][] newArticles = new int[currentLength + 1][3];
            for (int i = 0; i < currentLength; i++) {
                System.arraycopy(articles[i], 0, newArticles[i], 0, 3);
            }
            return newArticles;
        }
        return articles;
    }

    public static void printSales(int[][] sales, Date[] salesDate) {
        for (int i = 0; i < sales.length; i++) {
            if (salesDate[i] != null) {
                System.out.println(salesDate[i].toString() + " , " + sales[i][0] + " , " + sales[i][1] + " , " + sales[i][2]);
            } else {
                break;
            }
        }
    }

    public static void sortedTable(int[][] sales, Date[] salesDate) {
        /**
         * Sorting the sales and salesDate tables are not necessary anymore as insertion in those arrays are maintained by binary search,
         * thus all the data are already sorted according to article number.
         * thus, only pritning the sales information
         */
        printSales(sales, salesDate);
    }
}