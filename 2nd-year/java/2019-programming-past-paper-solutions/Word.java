public class Word {
    public static boolean isPalindrome(String s) {
        String forwards = "";
        String backwards = "";

        for(int j = 0; j < s.length(); j++) {
            forwards += s.substring(j, j + 1);
            backwards += s.substring(s.length() - j - 1, s.length() - j);
        }
        return forwards.equals(backwards) ? true : false;
    }

    public static void showLetters(String word, String characters) {
        String output = "";

        for(int i = 0; i < word.length(); i++) {
            boolean charGuessed = false;
            for(int j = 0; j < characters.length(); j++) {
                if(word.substring(i, i + 1).equals(characters.substring(j, j + 1))) {
                    charGuessed = true;
                }
            }
            if(charGuessed == true) {
                output += word.substring(i, i + 1);
            }
            else {
                output += "_";
            }
        }
        System.out.println(output);
    }

    public static void main(String[] args) {
        Word.showLetters("computing", "gpo");

        String s1 = "madam";
        String s2 = "hi";
        System.out.println(Word.isPalindrome(s1));
        System.out.println(Word.isPalindrome(s2));
    }
}
