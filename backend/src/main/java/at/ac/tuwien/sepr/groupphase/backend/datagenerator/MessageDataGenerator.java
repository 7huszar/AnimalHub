package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

@Profile("generateData")
@Component
@RequiredArgsConstructor
public class MessageDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_MESSAGES_TO_GENERATE = 5;
    private static final String TEST_NEWS_TITLE = "Title";
    private static final String TEST_NEWS_SUMMARY = "Summary of the message";
    private static final String TEST_NEWS_TEXT = "This is the text of the message";

    private final MessageRepository messageRepository;

    @PostConstruct
    private void generateMessage() {
        if (messageRepository.findAll().size() > 0) {
            LOGGER.debug("message already generated");
        } else {
            LOGGER.debug("generating {} message entries", NUMBER_OF_MESSAGES_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_MESSAGES_TO_GENERATE; i++) {
                Message message = Message.builder()
                    .title(TEST_NEWS_TITLE + " " + i)
                    .summary(TEST_NEWS_SUMMARY + " " + i)
                    .text(TEST_NEWS_TEXT + " " + i)
                    .publishedAt(LocalDateTime.now().minusMonths(i))
                    .build();
                LOGGER.debug("saving message {}", message);
                messageRepository.save(message);
            }
        }
    }

}
