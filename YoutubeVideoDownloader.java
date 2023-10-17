import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class YoutubeVideoDownloader extends JFrame {

    private JTextField urlField;
    private JButton downloadButton;

    public YoutubeVideoDownloader() {
        setTitle("YouTube Video Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        urlField = new JTextField(30);
        downloadButton = new JButton("Download");

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String videoUrl = urlField.getText();
                downloadVideo(videoUrl);
            }
        });

        add(new JLabel("Video URL:"));
        add(urlField);
        add(downloadButton);
    }

    private void downloadVideo(String videoUrl) {
        try {
            URL url = new URL(videoUrl);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            String fileName = getFileNameFromUrl(videoUrl);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(fileName));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();

                FileOutputStream fos = new FileOutputStream(savePath);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                fos.close();
                JOptionPane.showMessageDialog(this, "Download complete!");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error downloading video: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFileNameFromUrl(String videoUrl) {
        String[] urlParts = videoUrl.split("/");
        String videoId = urlParts[urlParts.length - 1].split("\\?")[0];
        return videoId + ".mp4";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new YoutubeVideoDownloader().setVisible(true);
            }
        });
    }
}
