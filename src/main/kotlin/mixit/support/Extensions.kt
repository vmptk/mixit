package mixit.support

import org.reactivestreams.Publisher
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.ResourcePropertySource
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.query.MongoEntityInformation
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactory
import org.springframework.http.ReactiveHttpOutputMessage
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.Serializable
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass


fun ConfigurableEnvironment.addPropertySource(location: String) {
    propertySources.addFirst(ResourcePropertySource(EncodedResource(ClassPathResource(location), StandardCharsets.UTF_8)))
}

fun <T : Any> ClientResponse.bodyToFlux(type: KClass<T>) : Flux<T> = bodyToFlux(type.java)

fun <T : Any> ClientResponse.bodyToMono(type: KClass<T>) : Mono<T> = bodyToMono(type.java)

inline fun <reified T : Publisher<S>, reified S : Any> fromPublisher(publisher: T) : BodyInserter<T, ReactiveHttpOutputMessage> =
    BodyInserters.fromPublisher(publisher, S::class.java)

inline fun <reified T : Any> ClientResponse.bodyToFlux() = bodyToFlux(T::class.java)

inline fun <reified T : Any> ServerRequest.bodyToMono(type: KClass<T>) = bodyToMono(T::class.java)

inline fun <reified T : Any> ReactiveMongoTemplate.findById(id: Any) : Mono<T> = findById(id, T::class.java)

inline fun <reified T : Any> ReactiveMongoTemplate.find(query: Query) : Flux<T> = find(query, T::class.java)

inline fun <T : Any, ID : Serializable> ReactiveMongoRepositoryFactory.getEntityInformation(type: KClass<T>) : MongoEntityInformation<T, ID> = getEntityInformation(type.java)