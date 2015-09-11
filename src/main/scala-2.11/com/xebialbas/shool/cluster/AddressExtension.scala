package com.xebialbas.shool.cluster

import akka.actor.{ExtensionKey, ExtendedActorSystem, Extension}

class AddressExtension(system: ExtendedActorSystem) extends Extension {

  def address = system.provider.getDefaultAddress
}

object AddressExtension extends ExtensionKey[AddressExtension]
