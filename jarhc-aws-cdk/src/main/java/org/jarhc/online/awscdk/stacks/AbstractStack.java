package org.jarhc.online.awscdk.stacks;

import java.util.List;
import java.util.Map;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

abstract class AbstractStack extends Stack {

	static final String CLOUDFRONT_HOSTED_ZONE_ID = "Z2FDTNDATAQYW2";

	AbstractStack(Construct scope, String id, StackProps props) {
		super(scope, id, props);
	}

	void createOutput(String id, String value, String description) {
		CfnOutput.Builder.create(this, id)
				.value(value)
				.description(description)
				.build();
	}

	// helper methods ----------------------------------------------------------

	@SafeVarargs
	static <T> List<T> list(T... elements) {
		return List.of(elements);
	}

	static <K, V> Map<K, V> map() {
		return Map.of();
	}

	static <K, V> Map<K, V> map(K k1, V v1) {
		return Map.of(k1, v1);
	}

	static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
		return Map.of(k1, v1, k2, v2);
	}

	static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3) {
		return Map.of(k1, v1, k2, v2, k3, v3);
	}

	static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		return Map.of(k1, v1, k2, v2, k3, v3, k4, v4);
	}

	static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
	}

	@SuppressWarnings("SameParameterValue")
	static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
		return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
	}

	@SuppressWarnings("SameParameterValue")
	static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
		return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
	}

}
