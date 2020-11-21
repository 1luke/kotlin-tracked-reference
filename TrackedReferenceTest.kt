import org.junit.Test
import java.lang.ref.WeakReference

class TrackedReferenceTests {

    @Test
    fun testUnderlyingObjectIsTracked() {
        data class TestObject(val value: String = "")
        val testOject = TestObject()
        val tracker = TrackedReference(testOject)
        assert(tracker.underlyingObject === testOject)
    }

    @Test
    fun testStrongReferenceIsRetained() {
        data class TestObject(val value: String = "")
        var trackedObject: TrackedReference<Any>?
        var trackedObjectID: Int = -1
        run {
            val testOject = TestObject()
            trackedObject = TrackedReference(testOject)
            trackedObjectID = System.identityHashCode(testOject)
        }
        val underlyingObject = trackedObject!!.underlyingObject
        assert(trackedObjectID == System.identityHashCode(underlyingObject))
    }

    @Test
    fun testWeakReferenceIsNotRetained() {
        data class TestObject(val value: String = "")
        var trackedObject: TrackedReference<Any>?
        var trackedObjectID: Int = -1
        run {
            val testOject = TestObject()
            trackedObject = TrackedReference(WeakReference(testOject))
            trackedObjectID = System.identityHashCode(testOject)
        }
        val underlyingObject = trackedObject!!.underlyingObject
        assert(underlyingObject == null || underlyingObject is WeakReference<*>)
    }

    @Test
    fun testRemoveStrongReferenceAPI() {
        data class TestObject(val value: String = "")
        var trackedObject: TrackedReference<Any>?
        run {
            val testOject = TestObject()
            trackedObject = TrackedReference(testOject)
        }
        trackedObject!!.removeReference()
        val underlyingObject = trackedObject!!.underlyingObject
        assert(underlyingObject == null || underlyingObject is WeakReference<*>)
    }

}

