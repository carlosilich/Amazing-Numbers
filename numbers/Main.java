package numbers;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String WRONG_PROPERTY = "Available properties: [EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Amazing Numbers!\n");
        instructions();

        do {
            System.out.print("Enter a request: ");
            String input = scanner.nextLine();
            String[] numbers = input.split(" ");

            if (input.isEmpty()) {
                instructions();
            } else if (numbers.length == 1){
                long natNumber = Long.valueOf(numbers[0]);
                if (natNumber == 0) {
                    System.out.println("Goodbye!");
                    break;
                } else if (natNumber < 0) {
                    System.out.println("The first parameter should be a natural number or zero.");
                } else {
                    singleNumProperties(natNumber);
                }
            } else if (numbers.length == 2) {
                long start = Long.valueOf(numbers[0]);
                long end = Long.valueOf(numbers[1]);
                if (start == 0) {
                    System.out.println("Goodbye!");
                    break;
                } else if (end < 0) {
                    System.out.println("The second parameter should be a natural number");
                } else {
                    for (long i = start; i < start + end; i++) {
                        String[] numProperties = properties(i).split("-");
                        System.out.print(i + " is ");
                        for (int j = 0; j < numProperties.length; j++) {
                            System.out.print(numProperties[j]);
                        }
                        System.out.println();
                    }
                }
            } else if (numbers.length >= 3) {
                long start = Long.valueOf(numbers[0]);
                long end = Long.valueOf(numbers[1]);
                StringBuilder sb = new StringBuilder("The properties [");

                if (start == 0) {
                    System.out.println("Goodbye!");
                    break;
                } else if (end < 0) {
                    System.out.println("The second parameter should be a natural number");
                } else {
                    if (!arrayValidProperty(numbers)) {
                        if (numOfWrongProperties(numbers) == 1) {
                            System.out.println("The property [" + numbers[2].toUpperCase() + "] is wrong");
                            System.out.println(WRONG_PROPERTY);
                        } else {
                            for (int i = 2; i < numbers.length; i++) {
                                if (!isValidProperty(numbers[i].toLowerCase())) {
                                    sb.append(numbers[i].toUpperCase() + ", ");
                                }
                            }
                            sb.delete(sb.length() - 2, sb.length() - 1);
                            sb.deleteCharAt(sb.length() - 1);
                            sb.append("] are wrong.");
                            System.out.println(sb);
                            System.out.println(WRONG_PROPERTY);
                        }

                    } else if (arrayMutuallyExclusive(numbers) != null) {
                        String[] exclusiveProperties = arrayMutuallyExclusive(numbers);
                        String property1 = exclusiveProperties[0];
                        String property2 = exclusiveProperties[1];

                        System.out.printf("The request contains mutually exclusive properties: [%s, %s]%n", property1, property2);
                        System.out.println("There are no numbers with these properties.");
                    } else {
                        int n = 0;
                        while (n < end) {
                            for (int i = 2; i < numbers.length; i++) {
                                if (numberHasProperties(start, numbers)) {
                                    StringBuilder output = new StringBuilder();
                                    String[] numProperties = properties(start).split("-");
                                    for (String str : numProperties) {
                                      output.append(str);
                                  }
                                    System.out.println(start + " is " + output);
                                    n++;
                                }
                                start++;
                            }
                        }
                    }
                }
            }
        } while (true);
    }


    public static boolean numberHasProperties (long number, String[] properties) {
        boolean isAllProperties = true;
        for (int i = 2; i < properties.length; i++) {
            StringBuilder property = new StringBuilder(properties[i]);
            if (property.charAt(0) == '-') {
                property.deleteCharAt(0);
                if (isProperty(number, property.toString().toLowerCase())) {
                    return false;
                } else {
                    isAllProperties = isAllProperties && !isProperty(number, property.toString().toLowerCase());
                }
            } else {
                isAllProperties = isAllProperties && isProperty(number, property.toString().toLowerCase());
            }
        }
        return isAllProperties;
    }

    public static boolean isHappy (long number) {
        String numToStr = String.valueOf(number);
        int sum = 0;
        int counter = 0;

        while (true) {

            for (int i = 0; i < numToStr.length(); i++) {
                sum += Math.pow(Character.getNumericValue(numToStr.charAt(i)), 2);
            }

            counter++;

            if (sum == 1 || counter > 8) {
                break;
            } else {
                numToStr = String.valueOf(sum);
                sum = 0;
            }
        }
        return sum == 1;
    }

    public static boolean isSpy (long number) {
        String numToStr = String.valueOf(number);
        long sum = 0;
        long product = 1;
        for (int i = 0; i < numToStr.length(); i++) {
            sum += Character.getNumericValue(numToStr.charAt(i));
            product *=  Character.getNumericValue(numToStr.charAt(i));
        }
        return sum == product;
    }

    public static boolean areMutuallyExclusive (String property1, String property2) {
        String propertyA;
        String propertyB;

        if (property1.startsWith("-") && property2.contains("-")) {
            propertyA = property1.replace("-", "");
            propertyB = property2.replace("-", "");
        } else {
            propertyA = property1;
            propertyB = property2;
        }

        if (property1.equals(property2) || property2.equals(property1)) {
            return false;
        } else if (propertyA.equals("even") && propertyB.equals("odd") || property2.equals("even") && property1.equals("odd")) {
            return true;
        } else if (property1.equals("duck") && property2.equals("spy") || property2.equals("duck") && property1.equals("spy")) {
            return true;
        } else if (property1.equals("sunny") && property2.equals("square") || property2.equals("sunny") && property1.equals("square")) {
            return true;
        } else if (property1.equals("happy") && property2.equals("sad") || property2.equals("happy") && property1.equals("sad")) {
            return true;
        } else if (property1.replace("-", "").equals(property2.replace("-", ""))) {
            return true;
        }
        return false;
    }

    public static String[] arrayMutuallyExclusive (String[] properties) {
        String[] exclusiveProperties = new String[2];
        for (int i = 2; i < properties.length; i++) {
            for (int j = 2; j < properties.length; j++) {
                if (i == j) {
                } else if (areMutuallyExclusive(properties[i].toLowerCase(), properties[j].toLowerCase())) {
                    exclusiveProperties[0] = properties[i].toUpperCase();
                    exclusiveProperties[1] = properties[j].toUpperCase();
                    return exclusiveProperties;
                }
            }
        }
        return null;
    }

    public static boolean arrayValidProperty (String[] properties) {
        for (int i = 2; i < properties.length; i++) {
            if (!isValidProperty(properties[i].toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public static int numOfWrongProperties (String[] properties) {
        int numOfWrongProperties = 0;
        for (int i = 2; i < properties.length; i++) {
            if (!isValidProperty(properties[i].toLowerCase())) {
                numOfWrongProperties++;
            }
        }
        return numOfWrongProperties;
    }

    public static void instructions () {
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("\t * the first parameter represents a starting number;");
        System.out.println("\t * the second parameter shows how many consecutive numbers are to be printed;");
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.\n");
    }

    public static void singleNumProperties (long natNumber) {
        System.out.println("Properties of " + natNumber);
        System.out.println("buzz: " + isBuzz(natNumber));
        System.out.println("duck: " + isDuck(natNumber));
        System.out.println("palindromic: " + isPalindromic(natNumber));
        System.out.println("gapful: " + isGapful(natNumber));
        System.out.println("spy: " + isSpy(natNumber));
        System.out.println("square: " + isSquare(natNumber));
        System.out.println("sunny: " + isSunny(natNumber));
        System.out.println("jumping: " + isJumping(natNumber));
        System.out.println("happy: " + isHappy(natNumber));
        System.out.println("sad: " + !isHappy(natNumber));
        System.out.println("even: " + numParity(natNumber));
        System.out.println("odd: " + !numParity(natNumber));
    }

    public static boolean isProperty(long number, String property) {
        switch (property) {
            case "buzz":
                if (isBuzz(number)) {
                    return true;
                }
                break;
            case "duck":
                if (isDuck(number)) {
                    return true;
                }
                break;
            case "gapful":
                if (isGapful(number)) {
                    return true;
                }
                break;
            case "palindromic":
                if (isPalindromic(number)) {
                    return true;
                }
                break;
            case "spy":
                if (isSpy(number)) {
                    return true;
                }
                break;
            case "square":
                if (isSquare(number)) {
                    return true;
                }
                break;
            case "sunny":
                if (isSunny(number)) {
                    return true;
                }
                break;
            case "jumping":
                if (isJumping(number)) {
                    return true;
                }
                break;
            case "happy":
                if (isHappy(number)) {
                    return true;
                }
                break;
            case "sad":
                if (!isHappy(number)) {
                    return true;
                }
                break;
            case "even":
                if (numParity(number)) {
                    return true;
                }
                break;
            case "odd":
                if (!numParity(number)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public static String properties (long number) {
        String buzz = isBuzz(number) ? "buzz, " : "";
        String duck = isDuck(number) ? "duck, " : "";
        String gapful = isGapful(number) ? "gapful, " : "";
        String palindromic = isPalindromic(number) ? "palindromic, " : "";
        String spy = isSpy(number) ? "spy, " : "";
        String sunny = isSunny(number) ? "sunny, " : "";
        String square = isSquare(number) ? "square, " : "";
        String jumping = isJumping(number) ? "jumping, " : "";
        String happy = isHappy(number) ? "happy, " : "sad, ";
        String odd = numParity(number) ? "even" : "odd";
        return buzz + "-" + duck + "-" + gapful + "-" + palindromic + "-" + spy + "-" + square + "-" + sunny + "-" + jumping + "-" + happy + "-" + odd;
    }

    public static boolean isValidProperty (String property) {
        String[] properties = {"even", "odd", "buzz", "duck", "palindromic", "gapful", "spy", "square", "sunny", "jumping", "happy", "sad"};
        StringBuilder sb = new StringBuilder(property);
        String currentString = sb.charAt(0) == '-' ? sb.deleteCharAt(0).toString() : sb.toString();
        for (String str : properties) {
            if (str.equals(currentString)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSquare (long number) {
        int squareRootI = (int) Math.sqrt(number);
        double squareRootD = Math.sqrt(number);
        return squareRootI == squareRootD;
    }

    public static boolean isSunny (long number) {
        //long isSunnyNum = number + 1;
        int squareRootI = (int) Math.sqrt(number + 1);
        double squareRootD = Math.sqrt(number + 1);
        return squareRootI == squareRootD;
    }

    public static boolean isBuzz (long number) {
        return number % 7 == 0 || number % 10 == 7;
    }

    public static boolean numParity (long number) {
        return number % 2 == 0;
    }

    public static boolean isDuck (long number) {
        String newNumber = String.valueOf(number);
        return !newNumber.startsWith("0") && newNumber.contains("0");
    }

    public static boolean isPalindromic (long number) {
        String numToString = String.valueOf(number);
        int sizeOfNum = numToString.length();

        if (numToString.length() == 1) {
            return true;
        } else {
            for (int i = 0; i < sizeOfNum / 2; i++) {
                if (numToString.charAt(i) != numToString.charAt(sizeOfNum - 1 - i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isGapful (long number) {
        String numToString = String.valueOf(number);
        String[] indNums = numToString.split("");
        int sizeOfNum = numToString.length();

        if (sizeOfNum <= 2) {
            return false;
        } else if (number % (10 * Long.valueOf(indNums[0]) + Long.valueOf(indNums[sizeOfNum - 1])) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isJumping (long number) {
        String numToString = String.valueOf(number);
        String[] indNums = numToString.split("");
        int sizeOfNum = indNums.length;
        if (indNums.length == 1) {
            return true;
        } else if (indNums.length == 2) {
            if (Math.abs(Integer.valueOf(indNums[0]) - Integer.valueOf(indNums[1])) == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            for (int i = 1; i < sizeOfNum - 1; i++) {
                if (!(Math.abs(Integer.valueOf(indNums[i]) - Integer.valueOf(indNums[i-1])) == 1 && Math.abs(Integer.valueOf(indNums[i]) - Integer.valueOf(indNums[i + 1])) == 1)) {
                    return false;
                }
            }
        }
        return true;
    }
}
