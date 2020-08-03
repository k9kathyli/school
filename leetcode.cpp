// Given a string s, find the longest palindromic substring in s. You may assume that the maximum length of s is 1000. 

class Solution {
public:
    // Returns the longest palindrome in an input string
    // Time complexity: O(n^2), Space complexity: O(n) 
    std::string longestPalindrome(const std::string &input) {
        std::string maxPalindrome = input.substr(0,1);

        for (int i = 0; i < input.size(); ++i){    
            // Palindromes can have even or odd length, need to check both
            std::string maxPalindromeEven;
            std::string maxPalindromeOdd;
            
            // If two consecutive chars are the same, then it's an even length palindrome
            if (input[i] == input[i + 1]){
                maxPalindromeEven = expandPalindrome(i, i + 1, input);
            }
            
            maxPalindromeOdd = expandPalindrome(i, i, input);
              
            // Take the max length palindrome from the two
            if (maxPalindromeEven.size() > maxPalindrome.size()){
                maxPalindrome = maxPalindromeEven;
            }
            if (maxPalindromeOdd.size() > maxPalindrome.size()){
                maxPalindrome = maxPalindromeOdd;
                }
        }  
        return maxPalindrome;  
    }
    
    // Returns the longest palindrome given a "center" in string, which can be 1 or 2 characters
    std::string expandPalindrome(const int left, const int right, const std::string &input){
        
        std::string maxPalindrome = input.substr(left, right - left + 1);
        // Store left and rightmost indices of the palindrome as we expand around it
        int leftIndex = left; 
        int rightIndex = right;
        
        // Continually expand around center
        for (int j = 1; j <= left; ++j){
            if (input[left - j] == input[right + j]){
                // Increment both indices if the characters match
                --leftIndex;
                ++rightIndex;
            }else{
               break;
            }
        }
        return input.substr(leftIndex, rightIndex - leftIndex + 1);
    }
}; 
