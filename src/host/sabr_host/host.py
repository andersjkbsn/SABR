from sabr_host.packets import PacketIds, Packet
from sabr_host.bluetooth_connection import BluetoothConnection
from sabr_host.errors import NoPacketHandlerError
from sabr_host.target_info import TargetInfo


# Class used for making communication and target identification
# work together. Similar in responsibility to the Robot class on
# the NXT.
from sabr_host.tcp.client import Client


class Host(object):
    def __init__(self, nxt_name, tcp_host = None):
        self.host_name = nxt_name
        self.target_info = None
        self.connection = None

        # Set passthrough client
        if tcp_host is None:
            self.passthrough_client = None
        else:
            self.passthrough_client = Client(tcp_host[0], tcp_host[1])

    # When a TARGET_INFO_REQUEST packet is received, fetch
    # data from the targeting module, package it, and send
    # accross the same connection.
    def handle_target_request(self, packet):
        # Request target information from vision module
        bounding_boxes, frame_width = self.target_info.get_targets()
        print(f"Found {len(bounding_boxes)} targets")

        # Instantiate packet
        packet = Packet.instantiate_from_id(PacketIds.TARGET_INFO_REQUEST)

        # Insert data into packet
        packet.set_frame_width(int(frame_width))
        for box in bounding_boxes:
            packet.append_box(box.x_min, box.width, box.height)

        # Send packet
        self.connection.send_packet(packet)

    # Prints a debug string sent from the NXT
    def handle_debug(self, packet):
        print(f"[NXT/Debug] {packet.message}")

    # Mapping from Packet IDs to handler-methods
    # Handshake has no handler as it is handled by
    # 'BluetoothConnection.perform_handshake()'.
    id_handler_map = {PacketIds.TARGET_INFO_REQUEST: handle_target_request, PacketIds.DEBUG: handle_debug}

    # Query the id_handler_map for the appropriate method to run.
    def handle_packet(self, packet):
        if not Host.id_handler_map.__contains__(packet.get_id()):
            raise NoPacketHandlerError(packet.get_id())
        else:
            Host.id_handler_map[packet.get_id()](self, packet)

    # Establish a Bluetooth connection
    def connect(self):
        self.connection = BluetoothConnection()
        self.connection.connect(self.host_name)

    # Continuously check if packets are being received through
    # the Bluetooth connection. If they are, handle the packet.
    def handle_packets(self):
        # When connected, initialize target information
        self.target_info = TargetInfo(capture_device=1, debug=True, passthrough_client=self.passthrough_client)

        # Receive packets in a loop
        while True:
            packet = self.connection.receive_packet()

            self.handle_packet(packet)
