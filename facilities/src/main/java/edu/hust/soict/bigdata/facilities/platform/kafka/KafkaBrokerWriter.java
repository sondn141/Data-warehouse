package edu.hust.soict.bigdata.facilities.platform.kafka;

import edu.hust.soict.bigdata.facilities.common.config.Const;
import edu.hust.soict.bigdata.facilities.common.config.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class KafkaBrokerWriter implements AutoCloseable{

    private static final String KAFKA_DEFAULT_TOPIC = "test";

    private String topic;
    private Producer<String, byte[]> producer;

    private static final Logger logger = LoggerFactory.getLogger(KafkaBrokerWriter.class);

    public KafkaBrokerWriter(Properties props) {
        java.util.Properties kafkaProps = new java.util.Properties();
        kafkaProps.put("bootstrap.servers", props.getProperty(Const.KAFKA_BOOSTRAP_SERVERS));
        kafkaProps.put("acks", props.getProperty(Const.KAFKA_PRODUCER_ACKS));
        kafkaProps.put("retries", props.getProperty(Const.KAFKA_PRODUCER_RETRIES));
        kafkaProps.put("batch.size", props.getProperty(Const.KAFKA_PRODUCER_BATCH_SIZE));
        kafkaProps.put("linger.ms", props.getProperty(Const.KAFKA_PRODUCER_LINGER_MS));
        kafkaProps.put("buffer.memory", props.getProperty(Const.KAFKA_PRODUCER_BUFFER_MEMORY));
        kafkaProps.put("max.request.size", props.getProperty(Const.KAFKA_PRODUCER_MAX_REQUEST_SIZE));
        kafkaProps.put("key.serializer", props.getProperty(Const.KAFKA_PRODUCER_KEY_SERIALIZER));
        kafkaProps.put("value.serializer", props.getProperty(Const.KAFKA_PRODUCER_VALUE_SERIALIZER));

        this.topic = props.getProperty(Const.KAFKA_PRODUCER_TOPIC, KAFKA_DEFAULT_TOPIC);
        this.producer = new KafkaProducer<>(kafkaProps);
    }

    public Future<RecordMetadata> write(byte[] b) {
        logger.info("Sending a message to kafka");
        return producer.send(new ProducerRecord<>(topic, b));
    }

    public Future<RecordMetadata> write(byte[] b, String topic) {
        logger.info("Sending a message to kafka");
        return producer.send(new ProducerRecord<>(topic, b));
    }

    public Future<RecordMetadata> write(String k, String v) {
        logger.info("Sending a message to kafka");
        return producer.send(new ProducerRecord<>(topic, k, v.getBytes()));
    }

    public Future<RecordMetadata> write(String k, String v, String topic) {
        logger.info("Sending a message to kafka");
        return producer.send(new ProducerRecord<>(topic, k, v.getBytes()));
    }

    @Override
    public void close() {
        this.producer.close();
    }

    static {
        org.apache.log4j.Logger.getLogger("org").setLevel(Level.WARN);
        org.apache.log4j.Logger.getLogger("akka").setLevel(Level.WARN);
        org.apache.log4j.Logger.getLogger("kafka").setLevel(Level.WARN);
    }
}
