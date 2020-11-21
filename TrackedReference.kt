/**
 * A wrapper object used to fulfill parcelize and serialize requirements for reference types
 * without implementing the interfaces.
 * Object must be manually removed using `removeReference` when it is no longer needed to
 * prevent memory leak.
 *
 * The same object (not serialized) is stored internally and referenced via the public API.
 * Use WeakReference or make sure to manually remove the reference using `removeReference`
 */
@Parcelize @kotlinx.serialization.Serializable
data class TrackedReference<ReferenceType: Any> private constructor(
        private var uniqueID: Int = -1
) : Serializable, Parcelable {
    constructor(reference: ReferenceType) : this() {
        check(!(reference is TrackedReference<*>))
        uniqueID = System.identityHashCode(this)
        references[uniqueID] = reference
    }

    val underlyingObject: ReferenceType?
        get() = references.get(uniqueID) as? ReferenceType

    fun removeReference() {
        underlyingObject?.let { references.set(uniqueID, WeakReference(it)) }
    }

    companion object {
        var references = hashMapOf<Int, Any>()
            private set
    }
}
