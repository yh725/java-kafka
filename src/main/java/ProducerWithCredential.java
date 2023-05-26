import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerWithCredential {

	private final static String BOOTSTRAP_SERVER = "localhost:9092";

	private final static String TOPIC_NAME = "topic4";

	public static void main(String[] args) throws ExecutionException, InterruptedException {

		Properties configs = new Properties();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configs.put("security.protocol", "SASL_PLAINTEXT");
		configs.put("sasl.mechanism", "SCRAM-SHA-256");
		configs.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username='alice' password='alice-password';");

		KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

		String message = "Third Message";

		ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, message);

		RecordMetadata metadata = producer.send(record).get();

		System.out.printf(">>> %s, %d, %d", message, metadata.partition(), metadata.offset());

		producer.flush();
		producer.close();
	}
}
