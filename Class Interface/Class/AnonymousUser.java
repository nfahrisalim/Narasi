public class AnonymousUser {
    private String anonymousId;

        // setter
        public void setAnonymousId(String anonymousId) {
            this.anonymousId = anonymousId;
        }
        // getter
        public String getAnonymousId() {
            return anonymousId;
        }
    
        public void publishWork() {
            // method
            throw new UnsupportedOperationException("Anonymous user does not have permission to perform this action.");
        } 
    
        public void manageWork() {
            // method
            throw new UnsupportedOperationException("Anonymous user does not have permission to perform this action.");
        }
}


// // noted : class sesuai WORDS (memperluas kelas User dari aipun)
// public class AnonymousUser extends User {
//     private String anonymousId;

//     // setter
//     public void setAnonymousId(String anonymousId) {
//         this.anonymousId = anonymousId;
//     }
//     // getter
//     public String getAnonymousId() {
//         return anonymousId;
//     }

//     public void publishWork() {
//         // Add your code here
//         throw new UnsupportedOperationException("Anonymous user does not have permission to perform this action.");
//     } 

//     public void manageWork() {
//         // Add your code here
//         throw new UnsupportedOperationException("Anonymous user does not have permission to perform this action.");
//     }
// }
