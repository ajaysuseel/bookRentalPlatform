package bookrental;

import bookrental.service.BookRentalPlatform;
import bookrental.ui.PlatformUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        BookRentalPlatform platform = new BookRentalPlatform();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PlatformUI ui = new PlatformUI(platform);
                ui.setVisible(true);
            }
        });
    }
}
