public class LibraryRecord {

    public String recordType;           // New Book Entry or Borrow Request
    public String recordId;             // Unique ID generated automatically
    public String date;                 // Creation date
    public String studentId;            // Student who created it
    public String status;               // Available, Requested, Borrowed, Returned
    public String librarianId;          // Assigned librarian (initially empty)

    public LibraryRecord(String recordType, String recordId, String date,
                         String studentId, String status, String librarianId) {

        this.recordType = recordType;
        this.recordId = recordId;
        this.date = date;
        this.studentId = studentId;
        this.status = status;
        this.librarianId = librarianId;
    }

    @Override
    public String toString() {
        return "RecordID: " + recordId +
               ", Type: " + recordType +
               ", Date: " + date +
               ", Student: " + studentId +
               ", Status: " + status +
               ", Librarian: " + librarianId;
    }
}
